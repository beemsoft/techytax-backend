package org.techytax.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQuery;
import java.math.BigDecimal;
import java.math.BigInteger;

@Entity
@NamedQuery(name = Office.GET, query = "SELECT s FROM Office s WHERE s.user = :user")
@DiscriminatorValue("S")
@Data
@EqualsAndHashCode(callSuper = true)
public class Office extends Activum {
	
	public static final String GET = "Office.GET";

	private BigDecimal startupCosts;
	
	private int nofSquareMetersBusiness;
	private int nofSquareMetersPrivate;
	
	private BigInteger wozValue;
	
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
