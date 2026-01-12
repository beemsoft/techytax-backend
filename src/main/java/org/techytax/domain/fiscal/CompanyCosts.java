package org.techytax.domain.fiscal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.techytax.domain.Cost;
import org.techytax.domain.CostConstants;
import org.techytax.helper.ActivaHelper;
import org.techytax.helper.AmountHelper;
import org.techytax.repository.CostRepository;

import org.techytax.model.security.User;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Collection;

import static java.math.BigDecimal.ZERO;

@Component
@Data
public class CompanyCosts {

  public static final LocalDate YEAR_START = LocalDate.now().minusYears(1).withDayOfYear(1);
  public static final LocalDate YEAR_END = LocalDate.now().withDayOfYear(1).minusDays(1);

  @Autowired
  @JsonIgnore
  private CostRepository costRepository;

  @Autowired
  @JsonIgnore
  private ActivaHelper activaHelper;

  public BigInteger carCosts = BigInteger.ZERO;
  private BigInteger carAndTransportCosts = BigInteger.ZERO;
  private BigInteger otherCosts = BigInteger.ZERO;
  private BigInteger officeCosts = BigInteger.ZERO;

  private Collection<Cost> carCostList;
  private Collection<Cost> transportCostList;
  private Collection<Cost> generalCostList;
  private Collection<Cost> foodCostList;
  private Collection<Cost> officeCostList;

  private BigInteger vatCorrectionForPrivateUsage = BigInteger.ZERO;

  private BigInteger totalCost;

  void calculate(User user) {
    transportCostList = costRepository.findCosts(user, CostConstants.TRAVEL_WITH_PUBLIC_TRANSPORT, YEAR_START, YEAR_END);
    BigDecimal totalTransportCosts = ZERO;
    for (Cost cost: transportCostList) {
      totalTransportCosts = totalTransportCosts.add(cost.getAmount());
    }
    carCostList = costRepository.findCosts(user, CostConstants.BUSINESS_CAR, YEAR_START, YEAR_END);
    BigDecimal totalBusinessCarCosts = ZERO;
    for (Cost cost: carCostList) {
      totalBusinessCarCosts = totalBusinessCarCosts.add(cost.getAmount());
    }
    vatCorrectionForPrivateUsage = vatCorrectionForPrivateUsage.add(activaHelper.getVatCorrectionForPrivateUsageBusinessCar(user));
    totalBusinessCarCosts = totalBusinessCarCosts.add(new BigDecimal(vatCorrectionForPrivateUsage));

    carCosts = AmountHelper.roundToInteger(totalBusinessCarCosts);
    carAndTransportCosts = AmountHelper.roundToInteger(totalBusinessCarCosts.add(totalTransportCosts));
    officeCostList = costRepository.findCosts(user, CostConstants.SETTLEMENT, YEAR_START, YEAR_END);
    BigDecimal totalOfficeCosts = ZERO;
    for (Cost cost: officeCostList) {
      totalOfficeCosts = totalOfficeCosts.add(cost.getAmount());
    }
    officeCosts = AmountHelper.roundToInteger(totalOfficeCosts);
    generalCostList = costRepository.findCosts(user, CostConstants.EXPENSE_CURRENT_ACCOUNT, YEAR_START, YEAR_END);
    BigDecimal totalOtherCosts = ZERO;
    for (Cost cost: generalCostList) {
      if (cost.getAmount() != null) {
        totalOtherCosts = totalOtherCosts.add(cost.getAmount());
      }
    }
    foodCostList = costRepository.findCosts(user, CostConstants.BUSINESS_FOOD, YEAR_START, YEAR_END);
    BigDecimal totalFoodCosts = ZERO;
    for (Cost cost: foodCostList) {
      totalFoodCosts = totalFoodCosts.add(cost.getAmount());
    }
    totalFoodCosts = totalFoodCosts.multiply(BigDecimal.valueOf(CostConstants.FOOD_TAXFREE_PERCENTAGE));
    otherCosts = AmountHelper.roundToInteger(totalOtherCosts.add(totalFoodCosts));

    totalCost = AmountHelper.roundToInteger(totalBusinessCarCosts.add(totalTransportCosts.add(totalFoodCosts.add(totalOfficeCosts.add(totalOtherCosts)))));
  }
}
