package org.techytax.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.techytax.domain.Activity;
import org.techytax.domain.Invoice;
import org.techytax.invoice.InvoiceCreator;
import org.techytax.model.security.User;
import org.techytax.repository.ActivityRepository;
import org.techytax.repository.InvoiceRepository;
import org.techytax.saas.domain.Registration;
import org.techytax.saas.repository.RegistrationRepository;
import org.techytax.security.JwtTokenUtil;
import org.techytax.security.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Collection;

@RestController
public class InvoiceRestController {

    private static final Logger log = LoggerFactory.getLogger(InvoiceRestController.class);

    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${my.local.techytax.dir}")
    private String techytaxDataDir;

    private final JwtTokenUtil jwtTokenUtil;

    private final InvoiceRepository invoiceRepository;
    private final RegistrationRepository registrationRepository;
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;

    private InvoiceCreator invoiceCreator;

    @Autowired
    public InvoiceRestController(JwtTokenUtil jwtTokenUtil,
                                 InvoiceRepository invoiceRepository,
                                 ActivityRepository activityRepository,
                                 InvoiceCreator invoiceCreator,
                                 RegistrationRepository registrationRepository,
                                 UserRepository userRepository) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.invoiceRepository = invoiceRepository;
        this.activityRepository = activityRepository;
        this.invoiceCreator = invoiceCreator;
        this.registrationRepository = registrationRepository;
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "auth/invoice", method = RequestMethod.GET)
    public Collection<Invoice> getInvoices(HttpServletRequest request) {
        User user = getUser(request);
        return invoiceRepository.findByUser(user);
    }

    @RequestMapping(value = "auth/invoice/latest-period", method = RequestMethod.GET)
    public Collection<Invoice> getInvoicesForLatestPeriod(HttpServletRequest request) {
        User user = getUser(request);
        return invoiceRepository.findInvoices(user, LocalDate.now().minusMonths(3).withDayOfMonth(1), LocalDate.now().withDayOfMonth(1).minusDays(1));
    }

    @RequestMapping(value = "auth/invoice", method = { RequestMethod.PUT, RequestMethod.POST })
    public void saveInvoice(HttpServletRequest request, @RequestBody Invoice invoice) {
        User user = getUser(request);
        invoice.setUser(user);
        invoiceRepository.save(invoice);
    }

    @RequestMapping(value = "auth/invoice/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> createInvoicePdf(HttpServletRequest request, @PathVariable Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice not found"));
        User user = getUser(request);
        Registration registration = registrationRepository.findByUser(user).stream().findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Registration not found"));
        byte[] contents = invoiceCreator.createPdfInvoice(invoice, registration);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        String filename = "output.pdf";
        headers.setAccessControlAllowOrigin("*");
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(contents, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "auth/invoice/send", method = RequestMethod.POST)
    public ResponseEntity.BodyBuilder sendInvoicePdf(HttpServletRequest request, @RequestBody Invoice invoice) throws Exception {
        User user = getUser(request);
        Registration registration = registrationRepository.findByUser(user).stream().findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Registration not found"));
        invoice.setUser(user);
        invoice.setSent(LocalDate.now());
        byte[] contents;
        if (registration.getCompanyData().getJobsInIndividualHealthcareNumber() == null) {
            contents = invoiceCreator.createPdfInvoice(invoice, registration);
        } else {
            Collection<Activity> activities = activityRepository.getActivitiesForProject(user, invoice.getProject().getId(), LocalDate.now().minusMonths(1).withDayOfMonth(1), LocalDate.now().withDayOfMonth(1).minusDays(1));
            contents = invoiceCreator.createPdfInvoiceForBig(invoice, registration, activities);
        }
        Path invoicesPath = Paths.get(techytaxDataDir, "invoices");
        if (Files.notExists(invoicesPath)) {
            Files.createDirectories(invoicesPath);
        }
        Path path = Paths.get(invoicesPath.toString(), "factuur_" + invoice.getInvoiceNumber() + ".pdf");
        Files.write(path, contents);
        log.info("Invoice saved: " + path.toAbsolutePath());
        invoiceRepository.save(invoice);
        return ResponseEntity.ok();
    }

    @RequestMapping(value = "auth/invoice/{id}", method = RequestMethod.DELETE)
    public void deleteInvoice(HttpServletRequest request, @PathVariable Long id) {
        invoiceRepository.deleteById(id);
    }

    private User getUser(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        return userRepository.findByUsername(username);
    }
}
