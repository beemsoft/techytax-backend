package org.techytax.domain.fiscal;

import lombok.Data;
import org.techytax.domain.BalanceType;
import org.techytax.domain.FiscalBalance;
import org.techytax.domain.PrepaidTax;
import org.techytax.domain.PrivateWithdrawal;
import org.techytax.report.domain.BalanceReport;
import org.techytax.report.helper.FiscalReportHelper;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import static java.math.BigInteger.ZERO;

@Data
public class FiscalOverview {

	private int jaar;

	private ProfitAndLoss profitAndLoss;
	private Map<BalanceType,FiscalBalance> activaMap;

	private Map<BalanceType,FiscalBalance> passivaMap;

	private FiscalPension fiscalPension;

	private BigInteger bookTotalBegin = ZERO;
	private BigInteger bookTotalEnd = ZERO;
	private BigInteger enterpriseCapital = ZERO;
	private BigInteger enterpriseCapitalPreviousYear = ZERO;
	private BigInteger investmentDeduction = ZERO;

	private PrivateWithdrawal onttrekking;
	private BigInteger oudedagsReserveMaximaal = ZERO;

	private PrepaidTax prepaidTax;
	private BigInteger privateDeposit = ZERO;



	private BigDecimal turnOverUnpaid = BigDecimal.ZERO;

//	public BalanceReport getActivaReport() {
//		return FiscalReportHelper.getActivaReport(activaMap);
//	}
//
//	public BalanceReport getPassivaReport() {
//		return FiscalReportHelper.getPassivaReport(passivaMap);
//	}

}
