package org.techytax.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.techytax.external.domain.ExternalCostType;

import javax.persistence.*;

import static org.techytax.domain.CostConstants.SETTLEMENT;

@Immutable
@Entity
@NamedQueries({ @NamedQuery(name = CostType.FOR_MATCHING, query = "SELECT ct FROM CostType ct WHERE ct.balansMeetellen = true OR ct IN :costTypes"),
		@NamedQuery(name = CostType.FOR_TYPES, query = "SELECT ct FROM CostType ct WHERE ct IN :costTypes") })
@Table(name = "kostensoort")
@Getter
@Setter
public class CostType {

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setOmschrijving(String omschrijving) {
		this.omschrijving = omschrijving;
	}

	public boolean isBijschrijving() {
		return bijschrijving;
	}

	public void setBijschrijving(boolean bijschrijving) {
		this.bijschrijving = bijschrijving;
	}

	public boolean isBtwVerrekenbaar() {
		return btwVerrekenbaar;
	}

	public void setBtwVerrekenbaar(boolean btwVerrekenbaar) {
		this.btwVerrekenbaar = btwVerrekenbaar;
	}

	public boolean isBalansMeetellen() {
		return balansMeetellen;
	}

	public void setBalansMeetellen(boolean balansMeetellen) {
		this.balansMeetellen = balansMeetellen;
	}

	public boolean isAftrekbaar() {
		return aftrekbaar;
	}

	public void setAftrekbaar(boolean aftrekbaar) {
		this.aftrekbaar = aftrekbaar;
	}

	public boolean isInvestering() {
		return investering;
	}

	public void setInvestering(boolean investering) {
		this.investering = investering;
	}

	public ExternalCostType getExternalCostType() {
		return externalCostType;
	}

	public void setExternalCostType(ExternalCostType externalCostType) {
		this.externalCostType = externalCostType;
	}

	public static final String FOR_MATCHING = "CostType.FOR_MATCHING";
	public static final String FOR_TYPES = "CostType.FOR_TYPES";

	@Id
	private long id = 0;

	private String omschrijving;

	private boolean bijschrijving;

	private boolean btwVerrekenbaar;

	private boolean balansMeetellen;

	private boolean aftrekbaar;

	private boolean investering;

	@JsonIgnore
	@OneToOne
	@JoinTable(name = "cost_type_export", joinColumns = { @JoinColumn(name = "cost_type_id") }, inverseJoinColumns = { @JoinColumn(name = "external_code", referencedColumnName = "code") })
	private ExternalCostType externalCostType;

	public CostType() {
		// default constructor required by JPA
	}

	public CostType(long id) {
		this.id = id;
	}

	public CostType(String id) {
		this.id = Long.parseLong(id);
	}

	public boolean isForSettlement() {
		return this.equals(SETTLEMENT);
	}

	public boolean isVatDeclarable() {
		return btwVerrekenbaar;
	}

	public String getOmschrijving() {
		if (omschrijving != null) {
			return omschrijving;
		} else {
			return "Onbekend";
		}
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof CostType)) {
			return false;
		}
		CostType other = (CostType) object;
		if (this.getId() != other.getId()) {
			return false;
		}
		return true;
	}
}
