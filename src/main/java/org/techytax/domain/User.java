package org.techytax.domain;

import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDate;

@MappedSuperclass
@Data
public class User implements Serializable {

	private static final long serialVersionUID = -374265857173724138L;

	@Id
	@GeneratedValue
	protected Long id;

	private String role;

	private boolean blocked;

	@Column(name = "company_address")
	private String companyAddress;

	@Column(name = "company_zipcode")
	private String companyZipCode;

	@Column(name = "company_city")
	private String companyCity;

	private String companyName;
	private String email;

	private String initials;
	private String prefix;
	private String surname;

	private LocalDate latestOnlineTime;

	private String password;
	private String username;
	private String phoneNumber;
	private String fiscalNumber;

	@Column(name = "kvk_nummer")
	private Long chamberOfCommerceNumber;

	@Enumerated(EnumType.ORDINAL)
	private VatPeriodType vatPeriodType = VatPeriodType.PER_QUARTER;

	private String accountNumber;

	public boolean isBusinessAccount() {
		return true;
	}

	public boolean isAdmin() {
		return role.equals("admin");
	}

	public boolean passwordMatch(String pwd) {
		return password.equals(pwd);
	}

	public String getFullName() {
		StringBuffer sb = new StringBuffer();
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
			return "user";
		}
	}

}
