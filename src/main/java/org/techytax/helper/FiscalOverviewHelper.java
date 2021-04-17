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

	private final PrivateWithdrawal privateWithdrawal;

	public FiscalOverviewHelper(ActivaHelper activaHelper, ProfitAndLoss profitAndLoss, PrivateWithdrawal privateWithdrawal) {
		this.activaHelper = activaHelper;
		this.profitAndLoss = profitAndLoss;
		this.privateWithdrawal = privateWithdrawal;
	}

	public FiscalOverview createFiscalOverview(String username) {
		int bookYear = DateHelper.getFiscalYear();
		FiscalOverview overview = new FiscalOverview();

		overview.setJaar(bookYear);

		Map<BalanceType, FiscalBalance> balanceMap = activaHelper.handleActiva(username);
		overview.setBalanceMap(balanceMap);
		overview.setOfficeBottomValue(activaHelper.getOfficeBottomValue(username));

		profitAndLoss.handleProfitAndLoss(username);
		overview.setProfitAndLoss(profitAndLoss);

		privateWithdrawal.determineWithDrawal(profitAndLoss, balanceMap);
		overview.setOnttrekking(privateWithdrawal);

		return overview;
	}
}
