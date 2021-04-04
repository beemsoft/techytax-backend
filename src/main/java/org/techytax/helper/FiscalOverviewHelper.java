package org.techytax.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.techytax.domain.BalanceType;
import org.techytax.domain.FiscalBalance;
import org.techytax.domain.PrivateWithdrawal;
import org.techytax.domain.fiscal.FiscalOverview;
import org.techytax.domain.fiscal.ProfitAndLoss;
import org.techytax.util.DateHelper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class FiscalOverviewHelper {

	@Autowired
	private ActivaHelper activaHelper;

	private FiscalOverview overview;

	@Autowired
	private ProfitAndLoss profitAndLoss;

	private int bookYear;
	private Map<BalanceType, FiscalBalance> passivaMap;

	public FiscalOverview createFiscalOverview(Date beginDatum, Date eindDatum, String username) throws Exception {
		bookYear = DateHelper.getFiscalYear();
		overview = new FiscalOverview();
		passivaMap = new HashMap<>();
		PrivateWithdrawal privatWithdrawal = new PrivateWithdrawal();

		overview.setJaar(bookYear);

		Map<BalanceType, FiscalBalance> activaMap = activaHelper.handleActiva(username);
		overview.setActivaMap(activaMap);
		overview.setOfficeBottomValue(activaHelper.getOfficeBottomValue(username));

		profitAndLoss.handleProfitAndLoss(privatWithdrawal, username);
		overview.setProfitAndLoss(profitAndLoss);

		overview.setPassivaMap(passivaMap);

		return overview;
	}
}
