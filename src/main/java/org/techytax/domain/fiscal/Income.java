package org.techytax.domain.fiscal;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.techytax.domain.Cost;
import org.techytax.domain.CostConstants;
import org.techytax.domain.Invoice;
import org.techytax.repository.CostRepository;
import org.techytax.repository.InvoiceRepository;

import org.techytax.model.security.User;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static java.math.BigInteger.ZERO;

@Data
@Component
public class Income {

  public BigInteger getNettoOmzet() {
    return nettoOmzet;
  }

  public void setNettoOmzet(BigInteger nettoOmzet) {
    this.nettoOmzet = nettoOmzet;
  }

  public List<Invoice> getInvoices() {
    return invoices;
  }

  public void setInvoices(List<Invoice> invoices) {
    this.invoices = invoices;
  }

  public Collection<Cost> getPaidInvoices() {
    return paidInvoices;
  }

  public void setPaidInvoices(Collection<Cost> paidInvoices) {
    this.paidInvoices = paidInvoices;
  }

  private BigInteger nettoOmzet = ZERO;

  private List<Invoice> invoices;

  private Collection<Cost> paidInvoices;

  @Autowired
  private InvoiceRepository invoiceRepository;

  @Autowired
  private CostRepository costRepository;

  public BigDecimal calculateNetIncome(User user) {
    invoices = (List<Invoice>) invoiceRepository.findInvoices(user, LocalDate.now().minusYears(1).withMonth(1).withDayOfMonth(1), LocalDate.now().withMonth(1).withDayOfMonth(1).minusDays(1));
    BigDecimal totalNetIncome = BigDecimal.ZERO;
    for (Invoice invoice : invoices) {
      totalNetIncome = totalNetIncome.add(BigDecimal.valueOf(invoice.getUnitsOfWork()).multiply(invoice.getProject().getRate()));
    }
    paidInvoices = costRepository.findCosts(user, CostConstants.INVOICE_PAID, LocalDate.now().minusYears(1).withMonth(1).withDayOfMonth(1), LocalDate.now().withMonth(1).withDayOfMonth(1).minusDays(1));
    for (Cost cost: paidInvoices) {
      totalNetIncome = totalNetIncome.add(cost.getAmount());
    }
    nettoOmzet = totalNetIncome.toBigInteger();
    return totalNetIncome;
  }
}
