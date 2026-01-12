package org.techytax.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.techytax.domain.Activum;
import org.techytax.domain.BalanceType;
import org.techytax.domain.Cost;
import org.techytax.repository.ActivumRepository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Component
public class InvestmentDeductionHelper {

	@Autowired
	private ActivumRepository activumDao;

	public BigInteger getInvestmentDeduction(List<Cost> costList) throws Exception {
		BigInteger totalInvestmentDeduction = BigInteger.ZERO;
		for (Cost cost : costList) {
			Activum activum = new Activum();
//			activum.setUsername(UserCredentialManager.getUsername());
//			activum.setCost(cost);
//			activum = activumDao.getActivumForCost(cost);
			if (activum != null && activum.getBalanceType() == BalanceType.MACHINERY) {
				totalInvestmentDeduction = totalInvestmentDeduction.add(calculateInvestmentDeduction(cost.getAmount()));
			}
		}
		return totalInvestmentDeduction;
	}

	private BigInteger calculateInvestmentDeduction(BigDecimal totalInvestment) {
		BigInteger totalInvestmentRounded = totalInvestment.setScale(0, BigDecimal.ROUND_UP).toBigInteger();
		if (totalInvestmentRounded.compareTo(BigInteger.valueOf(2300)) == -1 || totalInvestmentRounded.compareTo(BigInteger.valueOf(309693)) == 1) {
			return BigInteger.ZERO;
		}
		if (totalInvestmentRounded.compareTo(BigInteger.valueOf(2301)) == 1 && totalInvestmentRounded.compareTo(BigInteger.valueOf(55745)) == -1) {
			return new BigDecimal(totalInvestmentRounded).multiply(BigDecimal.valueOf(.28)).setScale(0, BigDecimal.ROUND_UP).toBigInteger();
		}
		if (totalInvestmentRounded.compareTo(BigInteger.valueOf(55746)) == 1 && totalInvestmentRounded.compareTo(BigInteger.valueOf(103231)) == -1) {
			return BigInteger.valueOf(15609);
		}
		if (totalInvestmentRounded.compareTo(BigInteger.valueOf(103232)) == 1 && totalInvestmentRounded.compareTo(BigInteger.valueOf(309693)) == -1) {
			return BigInteger.valueOf(15609).subtract(new BigDecimal(totalInvestmentRounded.subtract(BigInteger.valueOf(103231))).multiply(BigDecimal.valueOf(.0756)).setScale(0, BigDecimal.ROUND_UP)
					.toBigInteger());
		}
		return null;
	}

}
