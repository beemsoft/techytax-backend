package org.techytax.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Getter
@Setter
public class Project {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id = 0L;

	@NotNull
	private String user;

	@NotNull
	@ManyToOne
	private Customer customer;

	@NotNull
	private String code;

	private String projectDescription;

	private String activityDescription;

	private Date startDate;

	private Date endDate;

	@Column(precision=50, scale=2)
	private BigDecimal rate;

	@Column(precision=3, scale=2)
	private BigDecimal revenuePerc;

	private int paymentTermDays;

	@Enumerated(EnumType.ORDINAL)
	private VatType vatType = VatType.HIGH;

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Project)) {
			return false;
		}
		Project other = (Project)obj;
		return this.id.equals(other.id);
	}
}
