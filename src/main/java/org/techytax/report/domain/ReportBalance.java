/**
 * Copyright 2012 Hans Beemsterboer
 * 
 * This file is part of the TechyTax program.
 *
 * TechyTax is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * TechyTax is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with TechyTax; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.techytax.report.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
public class ReportBalance {

	public BigDecimal getAanschafKosten() {
		return aanschafKosten;
	}

	public void setAanschafKosten(BigDecimal aanschafKosten) {
		this.aanschafKosten = aanschafKosten;
	}

	public BigInteger getBookValueBegin() {
		return bookValueBegin;
	}

	public void setBookValueBegin(BigInteger bookValueBegin) {
		this.bookValueBegin = bookValueBegin;
	}

	public BigInteger getBookValueEnd() {
		return bookValueEnd;
	}

	public void setBookValueEnd(BigInteger bookValueEnd) {
		this.bookValueEnd = bookValueEnd;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigInteger getRestwaarde() {
		return restwaarde;
	}

	public void setRestwaarde(BigInteger restwaarde) {
		this.restwaarde = restwaarde;
	}

	private BigDecimal aanschafKosten;
	private BigInteger bookValueBegin;
	private BigInteger bookValueEnd;
	private String description;
	private BigInteger restwaarde;

}
