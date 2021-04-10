package org.techytax.helper;

import org.springframework.stereotype.Component;
import org.techytax.domain.BalanceType;
import org.techytax.domain.FiscalBalance;
import org.techytax.domain.PrivateWithdrawal;
import org.techytax.domain.fiscal.FiscalOverview;
import org.techytax.domain.fiscal.ProfitAndLoss;
import org.techytax.util.DateHelper;

import java.util.Map;

@Component
public class FiscalOverviewHelper {

	private final ActivaHelper activaHelper;

	private final ProfitAndLoss profitAndLoss;

	public FiscalOverviewHelper(ActivaHelper activaHelper, ProfitAndLoss profitAndLoss) {
		this.activaHelper = activaHelper;
		this.profitAndLoss = profitAndLoss;
	}

	public FiscalOverview createFiscalOverview(String username) throws Exception {
		int bookYear = DateHelper.getFiscalYear();
		FiscalOverview overview = new FiscalOverview();
		PrivateWithdrawal privatWithdrawal = new PrivateWithdrawal();

		overview.setJaar(bookYear);

		Map<BalanceType, FiscalBalance> balanceMap = activaHelper.handleActiva(username);
		overview.setBalanceMap(balanceMap);
		overview.setOfficeBottomValue(activaHelper.getOfficeBottomValue(username));

		profitAndLoss.handleProfitAndLoss(privatWithdrawal, username);
		overview.setProfitAndLoss(profitAndLoss);

		return overview;
	}
}
