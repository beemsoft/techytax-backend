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

  public BigInteger getRepurchase() {
    return repurchase;
  }

  public void setRepurchase(BigInteger repurchase) {
    this.repurchase = repurchase;
  }

  public BigInteger getProfit() {
    return profit;
  }

  public void setProfit(BigInteger profit) {
    this.profit = profit;
  }

  @Autowired
  @JsonIgnore
  InvoiceRepository invoiceRepository;

  public Income getIncome() {
    return income;
  }

  public void setIncome(Income income) {
    this.income = income;
  }

  public CompanyCosts getCompanyCosts() {
    return companyCosts;
  }

  public void setCompanyCosts(CompanyCosts companyCosts) {
    this.companyCosts = companyCosts;
  }

  public Depreciation getDepreciation() {
    return depreciation;
  }

  public void setDepreciation(Depreciation depreciation) {
    this.depreciation = depreciation;
  }

  public FinancialIncomeAndExpenses getFinancialIncomeAndExpenses() {
    return financialIncomeAndExpenses;
  }

  public void setFinancialIncomeAndExpenses(FinancialIncomeAndExpenses financialIncomeAndExpenses) {
    this.financialIncomeAndExpenses = financialIncomeAndExpenses;
  }

  public ExtraordinaryIncomeAndExpenses getExtraordinaryIncomeAndExpenses() {
    return extraordinaryIncomeAndExpenses;
  }

  public void setExtraordinaryIncomeAndExpenses(ExtraordinaryIncomeAndExpenses extraordinaryIncomeAndExpenses) {
    this.extraordinaryIncomeAndExpenses = extraordinaryIncomeAndExpenses;
  }

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
