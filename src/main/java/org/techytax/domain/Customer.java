package org.techytax.domain;

import lombok.Getter;
import lombok.Setter;

import org.techytax.model.security.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.math.BigInteger;

@Entity
@Getter
@Setter
public class Customer {

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigInteger getCommerceNr() {
		return commerceNr;
	}

	public void setCommerceNr(BigInteger commerceNr) {
		this.commerceNr = commerceNr;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public BigInteger getNumber() {
		return number;
	}

	public void setNumber(BigInteger number) {
		this.number = number;
	}

	public String getNumberExtension() {
		return numberExtension;
	}

	public void setNumberExtension(String numberExtension) {
		this.numberExtension = numberExtension;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getEmailInvoice() {
		return emailInvoice;
	}

	public void setEmailInvoice(String emailInvoice) {
		this.emailInvoice = emailInvoice;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	@Id
	@GeneratedValue
	protected Long id = 0L;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	private String name;

	private String description;

	private BigInteger commerceNr;

	private String address;

	private BigInteger number;

	private String numberExtension;

	private String postalCode;

	private String city;

	private String contact;

	private String emailInvoice;

	private String telephone;

	private String fax;

	private String website;

	public String getFullAddress() {
		StringBuffer fullAddress = new StringBuffer();
		fullAddress.append(address);
		if (number != null) {
			fullAddress.append(" ");
			fullAddress.append(number);
		}
		if (numberExtension != null) {
			fullAddress.append(" ");
			fullAddress.append(numberExtension);
		}
		fullAddress.append(", ");
		fullAddress.append(postalCode);
		fullAddress.append(", ");
		fullAddress.append(city);
		return fullAddress.toString();
	}
}
