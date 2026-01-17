package org.techytax.domain.fiscal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.techytax.repository.InvoiceRepository;

import org.techytax.model.security.User;

import java.math.BigInteger;

import static java.math.BigInteger.ZERO;

@Data
@Component
public class ProfitAndLoss {

  @Autowired
  @JsonIgnore
  InvoiceRepository invoiceRepository;

  @Autowired
  private Income income;

  private BigInteger repurchase = ZERO;

  @Autowired
  private Depreciation depreciation;

  @Autowired
  public CompanyCosts companyCosts;

  @Autowired
  private FinancialIncomeAndExpenses financialIncomeAndExpenses;

  @Autowired
  private ExtraordinaryIncomeAndExpenses extraordinaryIncomeAndExpenses;

  private BigInteger profit = ZERO;

  public void handleProfitAndLoss(User user) {
    handleTurnOver(user);

    companyCosts.calculate(user);
    financialIncomeAndExpenses.calculate(user);
    depreciation.handleDepreciations(user);

    profit = income.getNettoOmzet().subtract(companyCosts.getTotalCost().add(depreciation.getTotalDepreciation()));

  }

  private void handleTurnOver(User user) {
    income.calculateNetIncome(user);
  }
}
