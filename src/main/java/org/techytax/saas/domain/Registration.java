package org.techytax.saas.domain;

import lombok.Getter;
import lombok.Setter;
import org.techytax.domain.VatPeriodType;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Registration {

  @Entity
  @Getter
  @Setter
  public static class PersonalData {
    @Id
    @GeneratedValue
    Long id = 0L;
    String initials;
    String prefix;
    String surname;
    String email;
    String phoneNumber;

    public String getFullName() {
      StringBuilder sb = new StringBuilder();
      if (surname != null) {
        sb.append(initials);
        if (prefix != null) {
          sb.append(" ");
          sb.append(prefix);
        }
        sb.append(" ");
        sb.append(surname);
        return sb.toString();
      } else {
        return "";
      }
    }
  }

  @Entity
  @Getter
  @Setter
  public static class CompanyData {
    @Id
    @GeneratedValue
    Long id = 0L;
    String companyName;
    String address;
    String zipCode;
    String city;
    String accountNumber;
    Long chamberOfCommerceNumber;
    String bigNumber; // BIG Beroepen Individuele Gezondheidszorg

    public String getFullAddress() {
      StringBuilder sb = new StringBuilder();
      sb.append(address);
      if (zipCode != null) {
        sb.append(", ");
        sb.append(zipCode);
      }
      sb.append(", ");
      sb.append(city);
      return sb.toString();
    }
  }

  @Entity
  @Getter
  @Setter
  public static class FiscalData {
    @Id
    @GeneratedValue
    Long id = 0L;
    String vatNumber;
    VatPeriodType declarationPeriod;
  }

  @Id
  @GeneratedValue
  protected Long id = 0L;

  @NotNull
  private String user;

  @Transient
  private String password;

  LocalDate registrationDate;

  @OneToOne(cascade = {CascadeType.ALL})
  PersonalData personalData;

  @OneToOne(cascade = {CascadeType.ALL})
  CompanyData companyData;

  @OneToOne(cascade = {CascadeType.ALL})
  FiscalData fiscalData;
}
