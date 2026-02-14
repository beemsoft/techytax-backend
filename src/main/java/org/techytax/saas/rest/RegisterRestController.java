package org.techytax.saas.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

@RestController
public class RegisterRestController {

  private static final Logger log = LoggerFactory.getLogger(RegisterRestController.class);

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
    if (userRepository.findByUsername(registerUser.getUsername()) != null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Gebruiker bestaat al");
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
    log.info("addRegistration called for new user: {}", registerUser.getUsername());
  }

  @RequestMapping(value = "auth/register", method = RequestMethod.PUT)
  @Transactional
  public void updateUser(HttpServletRequest request, @RequestBody RegisterUser registerUser) {
    User user = getUser(request);
    user.setFirstname(registerUser.getFirstName());
    user.setLastname(registerUser.getLastName());
    userRepository.save(user);
    log.info("updateUser called by user: {}", user.getUsername());
  }

  @RequestMapping(value = "auth/register", method = RequestMethod.GET)
  public Registration getRegistration(HttpServletRequest request) {
    User user = getUser(request);
    Optional<Registration> registration = registrationRepository.findByUser(user).stream().findFirst();
    if (registration.isPresent()) {
      return registration.get();
    } else {
      Registration newRegistration = new Registration();
      newRegistration.setUser(user);
      registrationRepository.save(newRegistration);
      log.info("New registration created for user: {}", user.getUsername());
      return newRegistration;
    }
  }

  @Transactional
  @RequestMapping(value = "auth/register", method = RequestMethod.DELETE)
  public void deleteRegistration(HttpServletRequest request) {
    User user = getUser(request);
    activumRepository.deleteActivumsByUser(user);
    bookRepository.deleteBookValues(user);
    costMatchRepository.deleteCostMatchesByUser(user);
    costRepository.deleteCostsByUser(user);
    customerRepository.deleteCustomersByUser(user);
    invoiceRepository.deleteInvoicesByUser(user);
    projectRepository.deleteProjectsByUser(user);
    userRepository.deleteById(user.getId());
    registrationRepository.deleteRegistrationByUser(user);
    log.info("deleteRegistration called by user: {}", user.getUsername());
  }

  private User getUser(HttpServletRequest request) {
    String token = request.getHeader(tokenHeader);
    String username = jwtTokenUtil.getUsernameFromToken(token);
    return userRepository.findByUsername(username);
  }
}
