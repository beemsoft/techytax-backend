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
@Getter
@Setter
public class Registration {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public LocalDate getRegistrationDate() {
    return registrationDate;
  }

  public void setRegistrationDate(LocalDate registrationDate) {
    this.registrationDate = registrationDate;
  }

  public PersonalData getPersonalData() {
    return personalData;
  }

  public void setPersonalData(PersonalData personalData) {
    this.personalData = personalData;
  }

  public CompanyData getCompanyData() {
    return companyData;
  }

  public void setCompanyData(CompanyData companyData) {
    this.companyData = companyData;
  }

  public FiscalData getFiscalData() {
    return fiscalData;
  }

  public void setFiscalData(FiscalData fiscalData) {
    this.fiscalData = fiscalData;
  }

  public Registration() {
      this.companyData = new CompanyData();
      this.personalData = new PersonalData();
      this.fiscalData = new FiscalData();
  }

  @Entity
  @Table(name = "REGISTRATION_PERSONAL_DATA")
  @Data
  public static class PersonalData {
    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public String getInitials() {
      return initials;
    }

    public void setInitials(String initials) {
      this.initials = initials;
    }

    public String getFirstName() {
      return firstName;
    }

    public void setFirstName(String firstName) {
      this.firstName = firstName;
    }

    public String getPrefix() {
      return prefix;
    }

    public void setPrefix(String prefix) {
      this.prefix = prefix;
    }

    public String getSurname() {
      return surname;
    }

    public void setSurname(String surname) {
      this.surname = surname;
    }

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }

    public String getPhoneNumber() {
      return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
      this.phoneNumber = phoneNumber;
    }

    @Id
    @GeneratedValue
    Long id = 0L;
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
    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public String getCompanyName() {
      return companyName;
    }

    public void setCompanyName(String companyName) {
      this.companyName = companyName;
    }

    public String getAddress() {
      return address;
    }

    public void setAddress(String address) {
      this.address = address;
    }

    public String getZipCode() {
      return zipCode;
    }

    public void setZipCode(String zipCode) {
      this.zipCode = zipCode;
    }

    public String getCity() {
      return city;
    }

    public void setCity(String city) {
      this.city = city;
    }

    public String getAccountNumber() {
      return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
      this.accountNumber = accountNumber;
    }

    public Long getChamberOfCommerceNumber() {
      return chamberOfCommerceNumber;
    }

    public void setChamberOfCommerceNumber(Long chamberOfCommerceNumber) {
      this.chamberOfCommerceNumber = chamberOfCommerceNumber;
    }

    public String getJobsInIndividualHealthcareNumber() {
      return jobsInIndividualHealthcareNumber;
    }

    public void setJobsInIndividualHealthcareNumber(String jobsInIndividualHealthcareNumber) {
      this.jobsInIndividualHealthcareNumber = jobsInIndividualHealthcareNumber;
    }

    @Id
    @GeneratedValue
    Long id = 0L;
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
    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public String getVatNumber() {
      return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
      this.vatNumber = vatNumber;
    }

    public VatPeriodType getDeclarationPeriod() {
      return declarationPeriod;
    }

    public void setDeclarationPeriod(VatPeriodType declarationPeriod) {
      this.declarationPeriod = declarationPeriod;
    }

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
