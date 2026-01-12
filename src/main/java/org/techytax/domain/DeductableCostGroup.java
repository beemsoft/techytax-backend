package org.techytax.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DeductableCostGroup implements Comparable<DeductableCostGroup> {
	public void setAftrekbaarBedrag(BigDecimal aftrekbaarBedrag) {
		this.aftrekbaarBedrag = aftrekbaarBedrag;
	}

	public BigDecimal getAftrekbaarBedrag() {
		return aftrekbaarBedrag;
	}

	public void setKostenSoort(CostType kostenSoort) {
		this.kostenSoort = kostenSoort;
	}

	public CostType getKostenSoort() {
		return kostenSoort;
	}

	private BigDecimal aftrekbaarBedrag;

	private CostType kostenSoort;
	
	public int compareTo(DeductableCostGroup o) {
		return (int) (this.kostenSoort.getId() - o.kostenSoort.getId());
	}
	
	@Override
	public String toString() {
		return "type: "+kostenSoort.getId()+", amount: "+aftrekbaarBedrag;
	}

}
