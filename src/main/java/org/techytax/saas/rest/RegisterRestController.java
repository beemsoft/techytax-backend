package org.techytax.saas.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.techytax.domain.RegisterUser;
import org.techytax.model.security.Authority;
import org.techytax.model.security.AuthorityName;
import org.techytax.model.security.User;
import org.techytax.repository.ActivumRepository;
import org.techytax.repository.BookRepository;
import org.techytax.repository.CostMatchRepository;
import org.techytax.repository.CostRepository;
import org.techytax.repository.CustomerRepository;
import org.techytax.repository.InvoiceRepository;
import org.techytax.repository.ProjectRepository;
import org.techytax.saas.domain.Registration;
import org.techytax.saas.repository.RegistrationRepository;
import org.techytax.security.JwtTokenUtil;
import org.techytax.security.repository.AuthorityRepository;
import org.techytax.security.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Arrays;

@Slf4j
@RestController
public class RegisterRestController {

  @Value("${jwt.header}")
  private String tokenHeader;

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  @Autowired
  private RegistrationRepository registrationRepository;

  @Autowired
  private CostRepository costRepository;

  @Autowired
  private ActivumRepository activumRepository;

  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private CostMatchRepository costMatchRepository;

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private InvoiceRepository invoiceRepository;

  @Autowired
  private ProjectRepository projectRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AuthorityRepository authorityRepository;

  @RequestMapping(value = "register", method = RequestMethod.POST)
  @Transactional
  public void addRegistration(HttpServletRequest request, @RequestBody RegisterUser registerUser) {
    if (!registrationRepository.findByUser(registerUser.getUsername()).isEmpty()) {
      throw new RuntimeException("Gebruiker bestaat al");
    }
    User user = new User();
    String password = new BCryptPasswordEncoder().encode(registerUser.getPassword());
    user.setPassword(password);
    user.setUsername(registerUser.getUsername());
    Authority authority = authorityRepository.findByName(AuthorityName.ROLE_USER);
    user.setAuthorities(Arrays.asList(authority));
    user.setEnabled(Boolean.TRUE);
    user.setFirstname(registerUser.getFirstName());
    user.setLastname(registerUser.getLastName());
    user.setLastPasswordResetDate(LocalDate.now());
    userRepository.save(user);
    log.info("addRegistration called by user: {}", getUser(request));
  }

  @RequestMapping(value = "auth/register", method = RequestMethod.PUT)
  @Transactional
  public void updateRegistration(HttpServletRequest request, @RequestBody Registration registration) {
    String username = getUser(request);
    registration.setUser(username);
    registrationRepository.save(registration);
    log.info("updateRegistration called by user: {}", getUser(request));
  }

  @RequestMapping(value = "auth/register", method = RequestMethod.GET)
  public Registration getRegistration(HttpServletRequest request) {
    String username = getUser(request);
    Registration registration;
    try {
      registration = registrationRepository.findByUser(username).stream().findFirst().get();
    } catch (Exception e) {
      throw new RuntimeException("Vul eerst je gegevens in");
    }
    return registration;
  }

  @Transactional
  @RequestMapping(value = "auth/register", method = RequestMethod.DELETE)
  public void deleteRegistration(HttpServletRequest request) {
    String username = getUser(request);
    activumRepository.deleteActivumsByUser(username);
    bookRepository.deleteBookValues(username);
    costMatchRepository.deleteCostMatchesByUser(username);
    costRepository.deleteCostsByUser(username);
    customerRepository.deleteCustomersByUser(username);
    invoiceRepository.deleteInvoicesByUser(username);
    projectRepository.deleteProjectsByUser(username);
    userRepository.deleteUser(username);
    registrationRepository.deleteRegistrationByUser(username);
    log.info("deleteRegistration called by user: {}", getUser(request));
  }

  private String getUser(HttpServletRequest request) {
    String token = request.getHeader(tokenHeader);
    return jwtTokenUtil.getUsernameFromToken(token);
  }
}
