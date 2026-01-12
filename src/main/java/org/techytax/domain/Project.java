package org.techytax.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import org.techytax.model.security.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
public class Project {

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

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getProjectDescription() {
		return projectDescription;
	}

	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}

	public String getActivityDescription() {
		return activityDescription;
	}

	public void setActivityDescription(String activityDescription) {
		this.activityDescription = activityDescription;
	}

	public java.util.Date getStartDate() {
		return startDate;
	}

	public void setStartDate(java.util.Date startDate) {
		this.startDate = startDate;
	}

	public java.util.Date getEndDate() {
		return endDate;
	}

	public void setEndDate(java.util.Date endDate) {
		this.endDate = endDate;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public BigDecimal getRevenuePerc() {
		return revenuePerc;
	}

	public void setRevenuePerc(BigDecimal revenuePerc) {
		this.revenuePerc = revenuePerc;
	}

	public int getPaymentTermDays() {
		return paymentTermDays;
	}

	public void setPaymentTermDays(int paymentTermDays) {
		this.paymentTermDays = paymentTermDays;
	}

	public VatType getVatType() {
		return vatType;
	}

	public void setVatType(VatType vatType) {
		this.vatType = vatType;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
