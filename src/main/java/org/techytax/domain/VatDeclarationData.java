package org.techytax.domain;

import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDate;

import static java.math.BigInteger.ZERO;

@Data
public class VatDeclarationData {

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
