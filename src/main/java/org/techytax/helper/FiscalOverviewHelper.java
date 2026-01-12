package org.techytax.helper;

import org.springframework.stereotype.Component;
import org.techytax.domain.BalanceType;
import org.techytax.domain.FiscalBalance;
import org.techytax.domain.PrivateWithdrawal;
import org.techytax.domain.fiscal.FiscalOverview;
import org.techytax.domain.fiscal.FiscalPension;
import org.techytax.domain.fiscal.ProfitAndLoss;
import org.techytax.util.DateHelper;

import org.techytax.model.security.User;

import java.math.BigInteger;
import java.util.Map;

@Component
public class FiscalOverviewHelper {

	private final ActivaHelper activaHelper;
	private final ProfitAndLoss profitAndLoss;
	private final PrivateWithdrawal privateWithdrawal;
	private final FiscalPension fiscalPension;
	private BigInteger maximalFiscalPension;

	public FiscalOverviewHelper(ActivaHelper activaHelper, ProfitAndLoss profitAndLoss, PrivateWithdrawal privateWithdrawal, FiscalPension fiscalPension) {
		this.activaHelper = activaHelper;
		this.profitAndLoss = profitAndLoss;
		this.privateWithdrawal = privateWithdrawal;
		this.fiscalPension = fiscalPension;
	}

	public FiscalOverview createFiscalOverview(User user) {
		int bookYear = DateHelper.getFiscalYear();
		FiscalOverview overview = new FiscalOverview();

		overview.setJaar(bookYear);

		Map<BalanceType, FiscalBalance> balanceMap = activaHelper.handleActiva(user);
		overview.setBalanceMap(balanceMap);
		overview.setOfficeBottomValue(activaHelper.getOfficeBottomValue(user));

		profitAndLoss.handleProfitAndLoss(user);
		overview.setProfitAndLoss(profitAndLoss);

		privateWithdrawal.determineWithDrawal(profitAndLoss, balanceMap);
		overview.setOnttrekking(privateWithdrawal);

		this.maximalFiscalPension = fiscalPension.getMaximalFiscalPension(overview);

		return overview;
	}
}
