package org.techytax.domain.fiscal;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

import static java.math.BigInteger.ZERO;

@Component
@Data
public class ExtraordinaryIncomeAndExpenses {

  public BigInteger getOtherExtraordinaryIncome() {
    return otherExtraordinaryIncome;
  }

  public void setOtherExtraordinaryIncome(BigInteger otherExtraordinaryIncome) {
    this.otherExtraordinaryIncome = otherExtraordinaryIncome;
  }

  private BigInteger otherExtraordinaryIncome = ZERO;
}
