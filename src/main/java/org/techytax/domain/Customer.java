package org.techytax.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.techytax.model.security.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.math.BigInteger;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Customer {

	@Id
	@GeneratedValue
	@EqualsAndHashCode.Include
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
