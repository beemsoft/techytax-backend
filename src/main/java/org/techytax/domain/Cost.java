package org.techytax.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@NamedQueries({
		@NamedQuery(name = Cost.FOR_PERIOD, query = "SELECT c FROM Cost c WHERE c.user = :user AND c.date >= :beginDate AND c.date <= :endDate order by c.date asc"),
		@NamedQuery(name = Cost.FOR_PERIOD_AND_TYPES, query = "SELECT c FROM Cost c WHERE c.date >= :beginDate AND c.date <= :endDate AND c.costType IN :costTypes AND c.user = :user"),
		@NamedQuery(name = Cost.FOR_PERIOD_AND_VAT_DECLARABLE, query = "SELECT c FROM Cost c WHERE c.date >= :beginDate AND c.date <= :endDate AND c.costType.btwVerrekenbaar = true AND c.user = :user"),
		@NamedQuery(name = Cost.FOR_PERIOD_AND_ACCOUNT, query = "SELECT c FROM Cost c WHERE c.date >= :beginDate AND c.date <= :endDate AND c.costType.balansMeetellen = true AND c.user = :user") })
@Table(name = "cost")
@Getter
@Setter
public class Cost {

	static final String FOR_PERIOD = "Cost.FOR_PERIOD";
	static final String FOR_PERIOD_AND_TYPES = "Cost.FOR_PERIOD_AND_TYPES";
	static final String FOR_PERIOD_AND_VAT_DECLARABLE = "Cost.FOR_PERIOD_AND_VAT_DECLARABLE";
	static final String FOR_PERIOD_AND_ACCOUNT = "Cost.FOR_PERIOD_AND_ACCOUNT";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id = 0L;

	@NotNull
	private String user;

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
