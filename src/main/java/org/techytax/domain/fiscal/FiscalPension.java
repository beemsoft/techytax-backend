package org.techytax.domain.fiscal;

import org.springframework.stereotype.Component;
import org.techytax.domain.BalanceType;
import org.techytax.domain.FiscalBalance;

import java.math.BigDecimal;
import java.math.BigInteger;

@Component
public class FiscalPension {
  private static final BigInteger MAXIMAL_FISCAL_PENSION = BigInteger.valueOf(9218);
  private static final BigDecimal FOR_PERCENTAGE = BigDecimal.valueOf(0.0944f);

  /**
   * De toevoeging aan uw oudedagsreserve over een kalenderjaar is 9,44 % van de winst, met in 2020 een maximum van € 9.218.
   * De toevoeging vermindert u met de pensioenpremie die u al van de winst hebt afgetrokken.
   * De toevoeging is maximaal het bedrag waarmee het ondernemingsvermogen aan het einde van het kalenderjaar
   * uitkomt boven de oudedagsreserve aan het begin van het kalenderjaar.
   */
  public BigInteger getMaximalFiscalPension(FiscalOverview fiscalOverview) {
    BigInteger maximaleFor = new BigDecimal(fiscalOverview.getProfitAndLoss().getProfit()).multiply(FOR_PERCENTAGE).toBigInteger();
    if (maximaleFor.compareTo(MAXIMAL_FISCAL_PENSION) > 0) {
      maximaleFor = MAXIMAL_FISCAL_PENSION;
    }
    if (maximaleFor.compareTo(BigInteger.ZERO) < 0) {
      maximaleFor = BigInteger.ZERO;
    }
    FiscalBalance nonCurrentAssets = fiscalOverview.getBalanceMap().get(BalanceType.NON_CURRENT_ASSETS);
    FiscalBalance fiscalPension = fiscalOverview.getBalanceMap().get(BalanceType.PENSION);
    BigInteger enterpriseCapital = nonCurrentAssets.getEndSaldo().add(fiscalPension.getEndSaldo());
    BigInteger maximalAddition = enterpriseCapital.subtract(fiscalPension.getBeginSaldo());
    System.out.println("Maximal fiscal pension: " + maximaleFor);
    System.out.println("Maximal addition: " + maximalAddition);
    return maximaleFor;
  }
}
