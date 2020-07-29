package org.techytax.rest;

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
import org.techytax.domain.Activity;
import org.techytax.domain.Invoice;
import org.techytax.invoice.InvoiceCreator;
import org.techytax.mail.MailHelper;
import org.techytax.repository.ActivityRepository;
import org.techytax.repository.InvoiceRepository;
import org.techytax.saas.domain.Registration;
import org.techytax.saas.repository.RegistrationRepository;
import org.techytax.security.JwtTokenUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Collection;

@RestController
public class InvoiceRestController {

    @Value("${jwt.header}")
    private String tokenHeader;

    private final JwtTokenUtil jwtTokenUtil;

    private final InvoiceRepository invoiceRepository;
    private final RegistrationRepository registrationRepository;
    private final ActivityRepository activityRepository;

    private InvoiceCreator invoiceCreator;
    private MailHelper mailHelper;

    @Autowired
    public InvoiceRestController(JwtTokenUtil jwtTokenUtil,
                                 InvoiceRepository invoiceRepository,
                                 ActivityRepository activityRepository,
                                 InvoiceCreator invoiceCreator,
                                 RegistrationRepository registrationRepository,
                                 MailHelper mailHelper) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.invoiceRepository = invoiceRepository;
        this.activityRepository = activityRepository;
        this.invoiceCreator = invoiceCreator;
        this.registrationRepository = registrationRepository;
        this.mailHelper = mailHelper;
    }

    @RequestMapping(value = "auth/invoice", method = RequestMethod.GET)
    public Collection<Invoice> getInvoices(HttpServletRequest request) {
        String username = getUser(request);
        return invoiceRepository.findByUser(username);
    }

    @RequestMapping(value = "auth/invoice/latest-period", method = RequestMethod.GET)
    public Collection<Invoice> getInvoicesForLatestPeriod(HttpServletRequest request) {
        String username = getUser(request);
        return invoiceRepository.findInvoices(username, LocalDate.now().minusMonths(3).withDayOfMonth(1), LocalDate.now().withDayOfMonth(1).minusDays(1));
    }

    @RequestMapping(value = "auth/invoice", method = { RequestMethod.PUT, RequestMethod.POST })
    public void saveInvoice(HttpServletRequest request, @RequestBody Invoice invoice) {
        String username = getUser(request);
        invoice.setUser(username);
        invoiceRepository.save(invoice);
    }

    @RequestMapping(value = "auth/invoice/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> createInvoicePdf(HttpServletRequest request, @PathVariable Long id) {
        Invoice invoice = invoiceRepository.findById(id).get();
        String username = getUser(request);
        Registration registration = registrationRepository.findByUser(username).stream().findFirst().get();
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
        String username = getUser(request);
        Registration registration = registrationRepository.findByUser(username).stream().findFirst().get();
        invoice.setUser(username);
        invoice.setSent(LocalDate.now());
        byte[] contents;
        if (registration.getCompanyData().getJobsInIndividualHealthcareNumber() == null) {
            contents = invoiceCreator.createPdfInvoice(invoice, registration);
        } else {
            Collection<Activity> activities = activityRepository.getActivitiesForProject(username, invoice.getProject().getId(), LocalDate.now().minusMonths(1).withDayOfMonth(1), LocalDate.now().withDayOfMonth(1).minusDays(1));
            contents = invoiceCreator.createPdfInvoiceForBig(invoice, registration, activities);
        }
        mailHelper.sendInvoice(invoice.getHtmlText(), invoice, contents, registration);
        invoiceRepository.save(invoice);
        return ResponseEntity.ok();
    }

    @RequestMapping(value = "auth/invoice/{id}/send", method = RequestMethod.POST)
    public ResponseEntity.BodyBuilder sendInvoicePdf(HttpServletRequest request, @PathVariable Long id, @RequestBody String htmlText) throws Exception {
        Invoice invoice = invoiceRepository.findById(id).get();
        String username = getUser(request);
        Registration registration = registrationRepository.findByUser(username).stream().findFirst().get();
        invoice.setUser(username);
        invoice.setSent(LocalDate.now());
        byte[] contents = invoiceCreator.createPdfInvoice(invoice, registration);
        mailHelper.sendInvoice(htmlText, invoice, contents, registration);
//        invoiceRepository.save(invoice);
        return ResponseEntity.ok();
    }

//    @RequestMapping(value = "auth/invoice/{id}/remind", method = RequestMethod.POST)
//    public ResponseEntity.BodyBuilder sendReminder(HttpServletRequest request, @PathVariable Long id, @RequestBody String htmlText) throws Exception {
//        Invoice invoice = invoiceRepository.findOne(id);
//        String username = getUser(request);
//        Registration registration = registrationRepository.findByUser(username).stream().findFirst().get();
//        MailHelper.sendReminder(htmlText, invoice, registration);
//        return ResponseEntity.ok();
//    }

    @RequestMapping(value = "auth/invoice/{id}", method = RequestMethod.DELETE)
    public void deleteInvoice(HttpServletRequest request, @PathVariable Long id) {
        invoiceRepository.deleteById(id);
    }

    private String getUser(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        return jwtTokenUtil.getUsernameFromToken(token);
    }
}
