package org.techytax.domain.fiscal;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.techytax.domain.Invoice;
import org.techytax.repository.InvoiceRepository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

import static java.math.BigInteger.ZERO;

@Data
@Component
public class Income {

  private BigInteger nettoOmzet = ZERO;

  private List<Invoice> invoices;

  @Autowired
  private InvoiceRepository invoiceRepository;

  public BigDecimal calculateNetIncome(String username) {
    invoices = (List<Invoice>) invoiceRepository.findInvoices(username, LocalDate.now().minusYears(1).withMonth(1).withDayOfMonth(1), LocalDate.now().withMonth(1).withDayOfMonth(1).minusDays(1));
    BigDecimal totalNetIncome = BigDecimal.ZERO;
    for (Invoice invoice : invoices) {
      totalNetIncome = totalNetIncome.add(BigDecimal.valueOf(invoice.getUnitsOfWork()).multiply(invoice.getProject().getRate()));
    }
    nettoOmzet = totalNetIncome.toBigInteger();
    return totalNetIncome;
  }
}