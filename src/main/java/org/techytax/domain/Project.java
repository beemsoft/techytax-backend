package org.techytax.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.techytax.model.security.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Project {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	protected Long id = 0L;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@NotNull
	@ManyToOne
	private Customer customer;

	@NotNull
	private String code;

	private String projectDescription;

	private String activityDescription;

	private Date startDate;

	private Date endDate;

	@Column(precision = 10, scale = 2)
	private BigDecimal rate;

	@Column(precision = 5, scale = 2)
	private BigDecimal revenuePerc;

	private int paymentTermDays;

	@Enumerated(EnumType.ORDINAL)
	private VatType vatType = VatType.HIGH;
}
