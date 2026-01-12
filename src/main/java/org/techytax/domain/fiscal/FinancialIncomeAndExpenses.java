package org.techytax.domain.fiscal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.techytax.domain.Cost;
import org.techytax.domain.CostConstants;
import org.techytax.helper.AmountHelper;
import org.techytax.repository.CostRepository;

import org.techytax.model.security.User;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Collection;

import static java.math.BigInteger.ZERO;

@Component
@Data
public class FinancialIncomeAndExpenses {

  public BigInteger getInterestFromBusinessSavings() {
    return interestFromBusinessSavings;
  }

  public void setInterestFromBusinessSavings(BigInteger interestFromBusinessSavings) {
    this.interestFromBusinessSavings = interestFromBusinessSavings;
  }

  public Collection<Cost> getInterestList() {
    return interestList;
  }

  public void setInterestList(Collection<Cost> interestList) {
    this.interestList = interestList;
  }

  @Autowired
  @JsonIgnore
  private CostRepository costRepository;

  private BigInteger interestFromBusinessSavings = ZERO;

  private Collection<Cost> interestList;

  public void calculate(User user) {
    interestList = costRepository.findCosts(user, CostConstants.INTEREST, LocalDate.now().minusYears(1).withDayOfYear(1), LocalDate.now().withDayOfYear(1).minusDays(1));
    BigDecimal totalInterest = BigDecimal.ZERO;
    for (Cost cost: interestList) {
      totalInterest = totalInterest.add(cost.getAmount());
    }
    interestFromBusinessSavings = AmountHelper.roundToInteger(totalInterest);
  }
}
