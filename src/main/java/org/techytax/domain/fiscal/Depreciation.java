package org.techytax.domain.fiscal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.techytax.domain.Activum;
import org.techytax.repository.ActivumRepository;

import org.techytax.model.security.User;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Collection;

import static java.math.BigInteger.ZERO;
import static org.techytax.domain.BalanceType.CAR;
import static org.techytax.domain.BalanceType.MACHINERY;
import static org.techytax.domain.BalanceType.OFFICE;

@Component
@Data
public class Depreciation {

  private BigInteger afschrijvingAuto;
  private BigInteger machineryDepreciation;
  private BigInteger settlementDepreciation;
  private BigInteger totalDepreciation;

  @Autowired
  @JsonIgnore
  private ActivumRepository activumRepository;

  private Collection<Activum> machineryList;
  private Collection<Activum> carList;
  private Collection<Activum> officeList;


  public BigInteger getTotalDepreciation() {
    return totalDepreciation;
  }

  void handleDepreciations(User user) {
    afschrijvingAuto = ZERO;
    machineryDepreciation = ZERO;
    settlementDepreciation = ZERO;
    machineryList = activumRepository.findActivums(user, MACHINERY, LocalDate.now().minusYears(1).withDayOfYear(1), LocalDate.now().withDayOfYear(1).minusDays(1));
    carList = activumRepository.findActivums(user, CAR, LocalDate.now().minusYears(1).withDayOfYear(1), LocalDate.now().withDayOfYear(1).minusDays(1));
    officeList = activumRepository.findActivums(user, OFFICE, LocalDate.now().minusYears(1).withDayOfYear(1), LocalDate.now().withDayOfYear(1).minusDays(1));

    for (Activum activum: machineryList) {
      machineryDepreciation = machineryDepreciation.add(activum.getDepreciation());
    }
    for (Activum activum: carList) {
      afschrijvingAuto = afschrijvingAuto.add(activum.getDepreciation());
    }
    for (Activum activum: officeList) {
      settlementDepreciation = settlementDepreciation.add(activum.getDepreciation());
    }
    totalDepreciation = machineryDepreciation.add(settlementDepreciation);
  }
}
