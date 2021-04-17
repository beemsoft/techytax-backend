package org.techytax.domain.fiscal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.techytax.repository.InvoiceRepository;

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

  public void handleProfitAndLoss(String username) {
    handleTurnOver(username);

    companyCosts.calculate(username);
    financialIncomeAndExpenses.calculate(username);
    depreciation.handleDepreciations(username);

    profit = income.getNettoOmzet().subtract(companyCosts.getTotalCost().add(depreciation.getTotalDepreciation()));

  }

  private void handleTurnOver(String username) {
    income.calculateNetIncome(username);
  }
}
