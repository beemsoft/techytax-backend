package org.techytax.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import org.techytax.model.security.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@NamedQueries({
		@NamedQuery(name = Cost.FOR_PERIOD, query = "SELECT c FROM Cost c WHERE c.user = :user AND c.date >= :beginDate AND c.date <= :endDate order by c.date asc"),
		@NamedQuery(name = Cost.FOR_PERIOD_AND_TYPES, query = "SELECT c FROM Cost c WHERE c.date >= :beginDate AND c.date <= :endDate AND c.costType IN :costTypes AND c.user = :user"),
		@NamedQuery(name = Cost.FOR_PERIOD_AND_VAT_DECLARABLE, query = "SELECT c FROM Cost c WHERE c.date >= :beginDate AND c.date <= :endDate AND c.costType.btwVerrekenbaar = true AND c.user = :user"),
		@NamedQuery(name = Cost.FOR_PERIOD_AND_ACCOUNT, query = "SELECT c FROM Cost c WHERE c.date >= :beginDate AND c.date <= :endDate AND c.costType.balansMeetellen = true AND c.user = :user") })
@Table(name = "cost")
public class Cost {

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

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getVat() {
		return vat;
	}

	public void setVat(BigDecimal vat) {
		this.vat = vat;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public CostType getCostType() {
		return costType;
	}

	public void setCostType(CostType costType) {
		this.costType = costType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	static final String FOR_PERIOD = "Cost.FOR_PERIOD";
	static final String FOR_PERIOD_AND_TYPES = "Cost.FOR_PERIOD_AND_TYPES";
	static final String FOR_PERIOD_AND_VAT_DECLARABLE = "Cost.FOR_PERIOD_AND_VAT_DECLARABLE";
	static final String FOR_PERIOD_AND_ACCOUNT = "Cost.FOR_PERIOD_AND_ACCOUNT";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id = 0L;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Column(precision = 10, scale = 2)
	private BigDecimal amount;

	@Column(precision = 10, scale = 2)
	// TODO: in subclass? anders overbodig in fiscal overview
	private BigDecimal vat = BigDecimal.ZERO;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate date;

	@ManyToOne
	private CostType costType;

	@Column(length = 500)
	private String description;

	public long getCostTypeId() {
		return costType.getId();
	}

	public boolean isIncoming() {
		return costType.isBijschrijving();
	}

	public boolean isInvestment() { return costType != null && costType.isInvestering(); }

	public void roundValues() {
		if (amount != null) {
			amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		if (vat != null) {
			vat = vat.setScale(2, BigDecimal.ROUND_HALF_UP);
		}
	}
}
