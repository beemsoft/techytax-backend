package org.techytax.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.techytax.security.JwtAuthenticationEntryPoint;
import org.techytax.security.JwtAuthenticationTokenFilter;

@SuppressWarnings("SpringJavaAutowiringInspection")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(this.userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationTokenFilter();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
          .cors().disable()
          // we don't need CSRF because our token is invulnerable
          .csrf().disable()

          .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()

          // don't create session
          .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

          .authorizeRequests()
          .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
          // allow anonymous resource requests
          .antMatchers(
            HttpMethod.GET,
            "/*.html",
            "/favicon.ico",
            "/**/*.html",
            "/**/*.css",
            "/**/*.js"
          ).permitAll()
          .antMatchers("/register").permitAll()
          .antMatchers("/auth/**").permitAll();

//        httpSecurity.authorizeRequests().antMatchers("/").permitAll().and()
//                .authorizeRequests().antMatchers("/console/**").permitAll().anyRequest().anonymous();
//
//        httpSecurity.csrf().disable();
//
//        httpSecurity.headers().frameOptions().disable();

        // Custom JWT based security filter
//        httpSecurity
//                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

        // disable page caching
        httpSecurity.headers().cacheControl();
    }
}
