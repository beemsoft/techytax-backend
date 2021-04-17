package org.techytax.domain;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.techytax.domain.fiscal.ProfitAndLoss;

import java.math.BigInteger;
import java.util.Map;

@Data
@Component
public class PrivateWithdrawal {

	private BigInteger totaleOnttrekking;

	public void determineWithDrawal(ProfitAndLoss profitAndLoss, Map<BalanceType, FiscalBalance> balanceMap) {
		FiscalBalance nonCurrent = balanceMap.get(BalanceType.NON_CURRENT_ASSETS);
		FiscalBalance pension = balanceMap.get(BalanceType.PENSION);

		BigInteger businessCapitalPreviousYear = nonCurrent.getBeginSaldo().add(pension.getBeginSaldo());
		BigInteger businessCapitalCurrentYear = nonCurrent.getEndSaldo().add(pension.getEndSaldo());
		totaleOnttrekking = businessCapitalCurrentYear.subtract(businessCapitalPreviousYear.add(profitAndLoss.getProfit().subtract(profitAndLoss.companyCosts.carCosts)));
	}
}
