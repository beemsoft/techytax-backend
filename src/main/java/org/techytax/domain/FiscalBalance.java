package org.techytax.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
public class FiscalBalance {

	public BigDecimal getTotalPurchaseCost() {
		return totalPurchaseCost;
	}

	public void setTotalPurchaseCost(BigDecimal totalPurchaseCost) {
		this.totalPurchaseCost = totalPurchaseCost;
	}

	public BigInteger getBeginSaldo() {
		return beginSaldo;
	}

	public void setBeginSaldo(BigInteger beginSaldo) {
		this.beginSaldo = beginSaldo;
	}

	public BigInteger getEndSaldo() {
		return endSaldo;
	}

	public void setEndSaldo(BigInteger endSaldo) {
		this.endSaldo = endSaldo;
	}

	public BigInteger getTotalRemainingValue() {
		return totalRemainingValue;
	}

	public void setTotalRemainingValue(BigInteger totalRemainingValue) {
		this.totalRemainingValue = totalRemainingValue;
	}

	/**
	 Inclusief of exclusief btw?
	 Als u de btw kunt verrekenen, moet u de kosten van het bedrijfsmiddel nemen exclusief btw.
	 Als u de btw niet kunt verrekenen, moet u de investering nemen inclusief btw.
	 */
	private BigDecimal totalPurchaseCost;
	
	private BigInteger beginSaldo;
	
	private BigInteger endSaldo;
	
	private BigInteger totalRemainingValue;

}
