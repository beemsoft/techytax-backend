package org.techytax.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Immutable;

import jakarta.persistence.*;

import static org.techytax.domain.CostConstants.SETTLEMENT;

@Immutable
@Entity
@NamedQueries({ @NamedQuery(name = CostType.FOR_MATCHING, query = "SELECT ct FROM CostType ct WHERE ct.balansMeetellen = true OR ct IN :costTypes"),
		@NamedQuery(name = CostType.FOR_TYPES, query = "SELECT ct FROM CostType ct WHERE ct IN :costTypes") })
@Table(name = "kostensoort")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CostType {

	public static final String FOR_MATCHING = "CostType.FOR_MATCHING";
	public static final String FOR_TYPES = "CostType.FOR_TYPES";

	@Id
	@EqualsAndHashCode.Include
	private long id = 0;

	private String omschrijving;

	private boolean bijschrijving;

	private boolean btwVerrekenbaar;

	private boolean balansMeetellen;

	private boolean aftrekbaar;

	private boolean investering;

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
}
