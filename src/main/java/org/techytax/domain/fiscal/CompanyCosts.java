package org.techytax.domain.fiscal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.techytax.domain.Activum;
import org.techytax.domain.BalanceType;
import org.techytax.domain.BusinessCar;
import org.techytax.domain.Cost;
import org.techytax.domain.CostConstants;
import org.techytax.helper.AmountHelper;
import org.techytax.repository.ActivumRepository;
import org.techytax.repository.CostRepository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Collection;

import static java.math.BigInteger.ZERO;

@Component
@Data
public class CompanyCosts {

  @Autowired
  @JsonIgnore
  private CostRepository costRepository;

  @Autowired
  @JsonIgnore
  private ActivumRepository activumRepository;

  public BigInteger carCosts = ZERO;
  private BigInteger carAndTransportCosts = ZERO;
  private BigInteger otherCosts = ZERO;
  private BigInteger officeCosts = ZERO;

  private Collection<Cost> carCostList;
  private Collection<Cost> transportCostList;
  private Collection<Cost> generalCostList;
  private Collection<Cost> foodCostList;
  private Collection<Cost> officeCostList;

  private BigInteger vatCorrectionForPrivateUsage;

  private BigInteger totalCost;

  void calculate(String username) {
    transportCostList = costRepository.findCosts(username, CostConstants.TRAVEL_WITH_PUBLIC_TRANSPORT, LocalDate.now().minusYears(1).withDayOfYear(1), LocalDate.now().withDayOfYear(1).minusDays(1));
    BigDecimal totalTransportCosts = BigDecimal.ZERO;
    for (Cost cost: transportCostList) {
      totalTransportCosts = totalTransportCosts.add(cost.getAmount());
    }
    carCostList = costRepository.findCosts(username, CostConstants.BUSINESS_CAR, LocalDate.now().minusYears(1).withDayOfYear(1), LocalDate.now().withDayOfYear(1).minusDays(1));
    BigDecimal totalBusinessCarCosts = BigDecimal.ZERO;
    for (Cost cost: carCostList) {
      totalBusinessCarCosts = totalBusinessCarCosts.add(cost.getAmount());
    }
    Collection<Activum> carList = activumRepository.findActivums(username, BalanceType.CAR, LocalDate.now().minusYears(1).withDayOfYear(1), LocalDate.now().withDayOfYear(1).minusDays(1));
    vatCorrectionForPrivateUsage = BigInteger.ZERO;
    for (Activum activum: carList) {
      vatCorrectionForPrivateUsage = vatCorrectionForPrivateUsage.add(((BusinessCar)activum).getVatCorrectionForPrivateUsage());
    }
    totalBusinessCarCosts = totalBusinessCarCosts.add(new BigDecimal(vatCorrectionForPrivateUsage));

    carCosts = AmountHelper.roundToInteger(totalBusinessCarCosts);
    carAndTransportCosts = AmountHelper.roundToInteger(totalBusinessCarCosts.add(totalTransportCosts));
    officeCostList = costRepository.findCosts(username, CostConstants.SETTLEMENT, LocalDate.now().minusYears(1).withDayOfYear(1), LocalDate.now().withDayOfYear(1).minusDays(1));
    BigDecimal totalOfficeCosts = BigDecimal.ZERO;
    for (Cost cost: officeCostList) {
      totalOfficeCosts = totalOfficeCosts.add(cost.getAmount());
    }
    officeCosts = AmountHelper.roundToInteger(totalOfficeCosts);
    generalCostList = costRepository.findCosts(username, CostConstants.EXPENSE_CURRENT_ACCOUNT, LocalDate.now().minusYears(1).withDayOfYear(1), LocalDate.now().withDayOfYear(1).minusDays(1));
    BigDecimal totalOtherCosts = BigDecimal.ZERO;
    for (Cost cost: generalCostList) {
      if (cost.getAmount() != null) {
        totalOtherCosts = totalOtherCosts.add(cost.getAmount());
      }
    }
    foodCostList = costRepository.findCosts(username, CostConstants.BUSINESS_FOOD, LocalDate.now().minusYears(1).withDayOfYear(1), LocalDate.now().withDayOfYear(1).minusDays(1));
    BigDecimal totalFoodCosts = BigDecimal.ZERO;
    for (Cost cost: foodCostList) {
      totalFoodCosts = totalFoodCosts.add(cost.getAmount());
    }
    totalFoodCosts = totalFoodCosts.multiply(BigDecimal.valueOf(CostConstants.FOOD_TAXFREE_PERCENTAGE));
    otherCosts = AmountHelper.roundToInteger(totalOtherCosts.add(totalFoodCosts));

    totalCost = AmountHelper.roundToInteger(totalBusinessCarCosts.add(totalTransportCosts.add(totalFoodCosts.add(totalOfficeCosts.add(totalOtherCosts)))));
  }
}
