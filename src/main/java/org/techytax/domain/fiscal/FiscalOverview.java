package org.techytax.domain.fiscal;

import lombok.Data;
import org.techytax.domain.BalanceType;
import org.techytax.domain.FiscalBalance;
import org.techytax.domain.PrepaidTax;
import org.techytax.domain.PrivateWithdrawal;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import static java.math.BigInteger.ZERO;

@Data
public class FiscalOverview {

	public int getJaar() {
		return jaar;
	}

	public void setJaar(int jaar) {
		this.jaar = jaar;
	}

	public ProfitAndLoss getProfitAndLoss() {
		return profitAndLoss;
	}

	public void setProfitAndLoss(ProfitAndLoss profitAndLoss) {
		this.profitAndLoss = profitAndLoss;
	}

	public BigInteger getOfficeBottomValue() {
		return officeBottomValue;
	}

	public void setOfficeBottomValue(BigInteger officeBottomValue) {
		this.officeBottomValue = officeBottomValue;
	}

	public Map<BalanceType, FiscalBalance> getBalanceMap() {
		return balanceMap;
	}

	public void setBalanceMap(Map<BalanceType, FiscalBalance> balanceMap) {
		this.balanceMap = balanceMap;
	}

	public FiscalPension getFiscalPension() {
		return fiscalPension;
	}

	public void setFiscalPension(FiscalPension fiscalPension) {
		this.fiscalPension = fiscalPension;
	}

	public BigInteger getBookTotalBegin() {
		return bookTotalBegin;
	}

	public void setBookTotalBegin(BigInteger bookTotalBegin) {
		this.bookTotalBegin = bookTotalBegin;
	}

	public BigInteger getBookTotalEnd() {
		return bookTotalEnd;
	}

	public void setBookTotalEnd(BigInteger bookTotalEnd) {
		this.bookTotalEnd = bookTotalEnd;
	}

	public BigInteger getEnterpriseCapital() {
		return enterpriseCapital;
	}

	public void setEnterpriseCapital(BigInteger enterpriseCapital) {
		this.enterpriseCapital = enterpriseCapital;
	}

	public BigInteger getEnterpriseCapitalPreviousYear() {
		return enterpriseCapitalPreviousYear;
	}

	public void setEnterpriseCapitalPreviousYear(BigInteger enterpriseCapitalPreviousYear) {
		this.enterpriseCapitalPreviousYear = enterpriseCapitalPreviousYear;
	}

	public BigInteger getInvestmentDeduction() {
		return investmentDeduction;
	}

	public void setInvestmentDeduction(BigInteger investmentDeduction) {
		this.investmentDeduction = investmentDeduction;
	}

	public PrivateWithdrawal getOnttrekking() {
		return onttrekking;
	}

	public void setOnttrekking(PrivateWithdrawal onttrekking) {
		this.onttrekking = onttrekking;
	}

	public BigInteger getOudedagsReserveMaximaal() {
		return oudedagsReserveMaximaal;
	}

	public void setOudedagsReserveMaximaal(BigInteger oudedagsReserveMaximaal) {
		this.oudedagsReserveMaximaal = oudedagsReserveMaximaal;
	}

	public PrepaidTax getPrepaidTax() {
		return prepaidTax;
	}

	public void setPrepaidTax(PrepaidTax prepaidTax) {
		this.prepaidTax = prepaidTax;
	}

	public BigInteger getPrivateDeposit() {
		return privateDeposit;
	}

	public void setPrivateDeposit(BigInteger privateDeposit) {
		this.privateDeposit = privateDeposit;
	}

	public BigDecimal getTurnOverUnpaid() {
		return turnOverUnpaid;
	}

	public void setTurnOverUnpaid(BigDecimal turnOverUnpaid) {
		this.turnOverUnpaid = turnOverUnpaid;
	}

	private int jaar;

	private ProfitAndLoss profitAndLoss;
	private BigInteger officeBottomValue;

	private Map<BalanceType, FiscalBalance> balanceMap;

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
}
