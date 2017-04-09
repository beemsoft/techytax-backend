/**
 * Copyright 2014 Hans Beemsterboer
 * 
 * This file is part of the TechyTax program.
 *
 * TechyTax is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * TechyTax is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with TechyTax; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.techytax.helper;

import org.techytax.domain.Activum;
import org.techytax.util.DateHelper;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDate;

public class DepreciationHelper {

	/* Belastingdienst: De afschrijving is per jaar maximaal 20% op de aanschafkosten van het bedrijfsmiddel.
	   Voor goodwill geldt een percentage van maximaal 10%.
	 */
	private static final int MINIMUM_NUMBER_OF_DEPRECIATION_YEARS = 5;

	public static BigInteger getDepreciation(Activum activum) {
		BigDecimal yearlyDepreciation = BigDecimal.ZERO;
		int fiscalYear = LocalDate.now().minusYears(1).getYear();
		int years = activum.getNofYearsForDepreciation();
		if ((years > 0 && activum.getEndDate() == null && (isYearForDeprecation(activum.getStartDate(), years)))) {
			BigDecimal maximumYearlyDepreciation = activum.getPurchasePrice().
					subtract(BigDecimal.valueOf(activum.getRemainingValue().intValue())).
					divide(BigDecimal.valueOf(MINIMUM_NUMBER_OF_DEPRECIATION_YEARS), 2,
					RoundingMode.HALF_UP);
			yearlyDepreciation = activum.getPurchasePrice().subtract(BigDecimal.valueOf(activum.getRemainingValue().intValue())).divide(BigDecimal.valueOf(years), 2,
					RoundingMode.HALF_UP);
			double proportion = 1.0d;
			if (fiscalYear == years + activum.getStartDate().getYear()) {
				proportion = activum.getStartDate().getMonthValue() / 12.0d;
			} else if (fiscalYear == activum.getStartDate().getYear()) {
				proportion = 1.0d - activum.getStartDate().getMonthValue() / 12.0d;
			} 
			yearlyDepreciation = yearlyDepreciation.multiply(BigDecimal.valueOf(proportion));
			if (yearlyDepreciation.compareTo(maximumYearlyDepreciation) == 1) {
				yearlyDepreciation = maximumYearlyDepreciation;
			}
		}
		return AmountHelper.roundToInteger(yearlyDepreciation);
	}

	public static BigInteger getValueAtEndOfFiscalYear(Activum activum) {
		boolean isFullyDeprecated;
		int fiscalYear = LocalDate.now().minusYears(1).getYear();
		int years = activum.getNofYearsForDepreciation();
		BigDecimal yearlyDepreciation;

		isFullyDeprecated = years == 0
			|| activum.getStartDate().plusYears(years).getYear() < fiscalYear
			|| (activum.getEndDate() != null && activum.getEndDate().getYear() < fiscalYear);

		if (isFullyDeprecated) {
			return activum.getRemainingValue();
		} else {
			BigDecimal maximumYearlyDepreciation = activum.getPurchasePrice().
				subtract(BigDecimal.valueOf(activum.getRemainingValue().intValue())).
				divide(BigDecimal.valueOf(MINIMUM_NUMBER_OF_DEPRECIATION_YEARS), 2,
					RoundingMode.HALF_UP);
			yearlyDepreciation = activum.getPurchasePrice().subtract(BigDecimal.valueOf(activum.getRemainingValue().intValue())).divide(BigDecimal.valueOf(years), 2,
				RoundingMode.HALF_UP);
			if (yearlyDepreciation.compareTo(maximumYearlyDepreciation) == 1) {
				yearlyDepreciation = maximumYearlyDepreciation;
			}

			BigDecimal deprecatedValue = activum.getPurchasePrice();
			BigDecimal depreciationForYear;
			int endYear = fiscalYear;
			if (activum.getEndDate() != null && activum.getEndDate().getYear() < fiscalYear) {
				endYear = activum.getEndDate().getYear();
			}
			for (int year = activum.getStartDate().getYear(); year <= endYear; year++) {
				double proportion = 1.0d;
				if (((year == fiscalYear && year == activum.getStartDate().getYear()) || year == activum.getStartDate().getYear()) && activum.getEndDate() == null) {
					proportion = 1.0d - activum.getStartDate().getMonthValue() / 12.0d;
				} else if (activum.getEndDate() != null && year == activum.getEndDate().getYear() && year != activum.getStartDate().getYear()) {
					proportion = activum.getStartDate().getMonthValue() / 12.0d;
				} else if (activum.getEndDate() != null && year == activum.getEndDate().getYear() && year == activum.getStartDate().getYear()) {
					proportion = (activum.getEndDate().getMonthValue() - activum.getStartDate().getMonthValue()) / 12.0d;
				}
				depreciationForYear = yearlyDepreciation.multiply(BigDecimal.valueOf(proportion));
				deprecatedValue = deprecatedValue.subtract(depreciationForYear);
			}

			return AmountHelper.roundToInteger(deprecatedValue);
		}
	}

	private static boolean isYearForDeprecation(LocalDate startDate, int years) {
		if (startDate.getMonth().getValue() > 0) {
			years++;
		}
		return DateHelper.getFiscalYear() >= startDate.getYear() && DateHelper.getFiscalYear() <= years + startDate.getYear();
	}

}
