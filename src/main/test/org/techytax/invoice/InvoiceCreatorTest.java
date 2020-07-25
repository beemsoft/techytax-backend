package org.techytax.invoice;

import org.techytax.domain.Activity;
import org.techytax.domain.Customer;
import org.techytax.domain.Invoice;
import org.techytax.domain.Project;
import org.techytax.domain.VatType;
import org.techytax.saas.domain.Registration;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InvoiceCreatorTest {
    public static void main(String[] args) throws IOException {
        InvoiceCreator invoiceCreator = new InvoiceCreator();
        Registration registration = new Registration();
        registration.getCompanyData().setCompanyName("Mijn bedrijf");
        registration.getCompanyData().setChamberOfCommerceNumber(123L);
        registration.getCompanyData().setBigNumber("321");

        Invoice invoice = new Invoice();
        invoice.setSent(LocalDate.now());
        invoice.setInvoiceNumber("1");
        invoice.setMonth("month");
        Project project = new Project();
        project.setPaymentTermDays(30);
        project.setRate(BigDecimal.valueOf(10));
        project.setVatType(VatType.HIGH);
        project.setRevenuePerc(BigDecimal.valueOf(37.5));
        invoice.setProject(project);
        invoice.setUnitsOfWork(8);
        Customer customer = new Customer();
        customer.setName("Klant");
        customer.setAddress("Klant adres");
        project.setCustomer(customer);
        List<Activity> activities = new ArrayList<>();
        Activity activity = new Activity();
        activity.setHours(BigDecimal.valueOf(4));
        activity.setActivityDescription("Werkzaamheden A");
        activity.setActivityDate(LocalDate.now().minusDays(30));
        activities.add(activity);
        activity = new Activity();
        activity.setHours(BigDecimal.valueOf(4));
        activity.setActivityDescription("Werkzaamheden B");
        activity.setActivityDate(LocalDate.now().minusDays(29));
        activities.add(activity);
        byte[] pdf = invoiceCreator.createPdfInvoiceForBig(invoice, registration, activities);
        Path path = Paths.get("test.pdf");
        Files.write(path, pdf);
    }
}
