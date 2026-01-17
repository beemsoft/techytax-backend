package org.techytax.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
public class FiscalBalance {

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
