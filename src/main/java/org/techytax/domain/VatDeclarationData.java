package org.techytax.domain;

import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDate;

import static java.math.BigInteger.ZERO;

@Data
public class VatDeclarationData {

	public String getFiscalNumber() {
		return fiscalNumber;
	}

	public void setFiscalNumber(String fiscalNumber) {
		this.fiscalNumber = fiscalNumber;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getInitials() {
		return initials;
	}

	public void setInitials(String initials) {
		this.initials = initials;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public BigInteger getValueAddedTaxOwed() {
		return valueAddedTaxOwed;
	}

	public void setValueAddedTaxOwed(BigInteger valueAddedTaxOwed) {
		this.valueAddedTaxOwed = valueAddedTaxOwed;
	}

	public BigInteger getValueAddedTaxOwedToBePaidBack() {
		return valueAddedTaxOwedToBePaidBack;
	}

	public void setValueAddedTaxOwedToBePaidBack(BigInteger valueAddedTaxOwedToBePaidBack) {
		this.valueAddedTaxOwedToBePaidBack = valueAddedTaxOwedToBePaidBack;
	}

	public BigInteger getValueAddedTaxPrivateUse() {
		return valueAddedTaxPrivateUse;
	}

	public void setValueAddedTaxPrivateUse(BigInteger valueAddedTaxPrivateUse) {
		this.valueAddedTaxPrivateUse = valueAddedTaxPrivateUse;
	}

	public BigInteger getTaxedTurnoverSuppliesServicesGeneralTariff() {
		return taxedTurnoverSuppliesServicesGeneralTariff;
	}

	public void setTaxedTurnoverSuppliesServicesGeneralTariff(BigInteger taxedTurnoverSuppliesServicesGeneralTariff) {
		this.taxedTurnoverSuppliesServicesGeneralTariff = taxedTurnoverSuppliesServicesGeneralTariff;
	}

	public BigInteger getValueAddedTaxSuppliesServicesGeneralTariff() {
		return valueAddedTaxSuppliesServicesGeneralTariff;
	}

	public void setValueAddedTaxSuppliesServicesGeneralTariff(BigInteger valueAddedTaxSuppliesServicesGeneralTariff) {
		this.valueAddedTaxSuppliesServicesGeneralTariff = valueAddedTaxSuppliesServicesGeneralTariff;
	}

	public BigInteger getValueAddedTaxOnInput() {
		return valueAddedTaxOnInput;
	}

	public void setValueAddedTaxOnInput(BigInteger valueAddedTaxOnInput) {
		this.valueAddedTaxOnInput = valueAddedTaxOnInput;
	}

	public BigInteger getTurnoverFromTaxedSuppliesFromCountriesWithinTheEC() {
		return turnoverFromTaxedSuppliesFromCountriesWithinTheEC;
	}

	public void setTurnoverFromTaxedSuppliesFromCountriesWithinTheEC(BigInteger turnoverFromTaxedSuppliesFromCountriesWithinTheEC) {
		this.turnoverFromTaxedSuppliesFromCountriesWithinTheEC = turnoverFromTaxedSuppliesFromCountriesWithinTheEC;
	}

	public BigInteger getValueAddedTaxOnSuppliesFromCountriesWithinTheEC() {
		return valueAddedTaxOnSuppliesFromCountriesWithinTheEC;
	}

	public void setValueAddedTaxOnSuppliesFromCountriesWithinTheEC(BigInteger valueAddedTaxOnSuppliesFromCountriesWithinTheEC) {
		this.valueAddedTaxOnSuppliesFromCountriesWithinTheEC = valueAddedTaxOnSuppliesFromCountriesWithinTheEC;
	}

	private String fiscalNumber;
	private String prefix;
	private String initials;
	private String surname;
	private String fullName;
	private String phoneNumber;
	private LocalDate startDate;
	private LocalDate endDate;

	/**
	 * Verschuldigde_omzetbelasting_2
	 */
	private BigInteger valueAddedTaxOwed = ZERO;

	/**
	 * Totaal_te_betalen_/_terug_te_vragen_2
	 */
	private BigInteger valueAddedTaxOwedToBePaidBack = ZERO;

	/**
	 * Omzetbelasting_over_privegebruik_1
	 */
	private BigInteger valueAddedTaxPrivateUse = ZERO;

	/**
	 * Omzetbelasting_leveringen/diensten_algemeen_tarief moet gelijk zijn aan
	 * Omzet_leveringen/diensten_belast_met_algemeen_tarief maal
	 * Percentage_algemene_tarief_omzetbelasting afgerond naar beneden
	 */
	private BigInteger taxedTurnoverSuppliesServicesGeneralTariff = ZERO;

	/**
	 * Omzetbelasting_leveringen/diensten_algemeen_tarief_2
	 */
	private BigInteger valueAddedTaxSuppliesServicesGeneralTariff = ZERO;

	/**
	 * Voorbelasting_1
	 */
	private BigInteger valueAddedTaxOnInput = ZERO;

	private BigInteger turnoverFromTaxedSuppliesFromCountriesWithinTheEC = ZERO;

	private BigInteger valueAddedTaxOnSuppliesFromCountriesWithinTheEC = ZERO;

}
