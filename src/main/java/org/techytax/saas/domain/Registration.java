package org.techytax.saas.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.Data;
import org.techytax.domain.VatPeriodType;

import org.techytax.model.security.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "REGISTRATION")
@Data
public class Registration {

  public Registration() {
      this.companyData = new CompanyData();
      this.personalData = new PersonalData();
      this.fiscalData = new FiscalData();
  }

  @Entity
  @Table(name = "REGISTRATION_PERSONAL_DATA")
  @Data
  public static class PersonalData {
    @Id
    @GeneratedValue
    Long id;
    String initials;
    String firstName;
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
  @Table(name = "REGISTRATION_COMPANY_DATA")
  @Data
  public static class CompanyData {
    @Id
    @GeneratedValue
    Long id;
    String companyName;
    String address;
    String zipCode;
    String city;
    String accountNumber;
    Long chamberOfCommerceNumber;
    @Column(name="BIG_NUMBER")
    String jobsInIndividualHealthcareNumber; // BIG Beroepen Individuele Gezondheidszorg

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
  @Table(name = "REGISTRATION_FISCAL_DATA")
  @Data
  public static class FiscalData {
    @Id
    @GeneratedValue
    Long id;
    String vatNumber;
    VatPeriodType declarationPeriod;
  }

  @Id
  @GeneratedValue
  protected Long id = 0L;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

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
