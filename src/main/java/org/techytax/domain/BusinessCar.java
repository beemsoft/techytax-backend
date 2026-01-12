package org.techytax.domain;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("C")
@Getter
@Setter
public class BusinessCar extends Activum {

    private static final Logger log = LoggerFactory.getLogger(BusinessCar.class);

	private BigInteger fiscalIncomeAddition;

	/**
	 * Vindt het privégebruik plaats later dan 4 jaar na het jaar waarin u de auto in gebruik hebt genomen? Dan neemt u
	 * 1,5% in plaats van 2,7% van de catalogusprijs van de auto, inclusief btw en bpm.
	 */
    public BigDecimal getVatCorrectionForPrivateUsage() {
        log.info("Calculate VAT correction for private usage business car");
        LocalDate currentDate = LocalDate.now();
        if (isTimeForVatCorrection(currentDate)) {
            LocalDate firstCarUsageDate = getStartDate();
            BigDecimal correctionPercentage;
            if (currentDate.getYear() - firstCarUsageDate.getYear() < 5) {
                correctionPercentage = new BigDecimal(".027");
            } else {
                correctionPercentage = new BigDecimal(".015");
            }
            return getPurchasePrice().multiply(correctionPercentage).setScale(0, BigDecimal.ROUND_UP);
        } else {
            return BigDecimal.ZERO;
        }
    }

    private boolean isTimeForVatCorrection(LocalDate currentDate) {
        return currentDate.isAfter(currentDate.withMonth(1).withDayOfMonth(1))
                && currentDate.isBefore(currentDate.withMonth(4).withDayOfMonth(1));
    }

}
