package org.techytax.domain;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQuery;
import java.math.BigDecimal;
import java.math.BigInteger;

@Entity
@NamedQuery(name = Office.GET, query = "SELECT s FROM Office s WHERE s.user = :user")
@DiscriminatorValue("S")
@Getter
@Setter
public class Office extends Activum {
	
	public static final String GET = "Office.GET";

	private BigDecimal startupCosts;
	
	private int nofSquareMetersBusiness;
	private int nofSquareMetersPrivate;
	
	private BigInteger wozValue;
	
	public BigInteger getTerrainValue() {
		return terrainValue;
	}

	private BigInteger terrainValue;
	
//	@Type(type = "encryptedInteger")
//	private BigInteger eigenWoningForfaitBusiness;
//	
//	@Type(type = "encryptedInteger")
//	private BigInteger eigenWoningForfaitPrivate;
//	
//	@Type(type = "encryptedInteger")
//	private BigInteger fictiefRendement;

}
