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
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.math.BigInteger;

@Entity
@NamedQueries({
		@NamedQuery(name = BookValue.HISTORY, query = "SELECT bv FROM BookValue bv WHERE bv.user = :user order by bv.balanceType asc, bv.bookYear desc"),
		@NamedQuery(name = BookValue.FOR_YEAR, query = "SELECT bv FROM BookValue bv WHERE bv.user = :user AND bv.bookYear = :bookYear order by bv.balanceType asc"),
		@NamedQuery(name = BookValue.GET, query = "SELECT bv FROM BookValue bv WHERE bv.bookYear = :bookYear and bv.user = :user and bv.balanceType = :balanceType"),
		@NamedQuery(name = BookValue.FOR_YEAR_AND_TYPES, query = "SELECT bv FROM BookValue bv WHERE bv.user = :user AND bv.bookYear = :bookYear AND bv.balanceType IN :balanceTypes ORDER BY bv.balanceType asc") })
@Table(name = "bookvalue")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BookValue {

	static final String HISTORY = "org.techytax.domain.BookValue.HISTORY";
	static final String FOR_YEAR = "org.techytax.domain.BookValue.FOR_YEAR";
	public static final String GET = "org.techytax.domain.BookValue.GET";
	static final String FOR_YEAR_AND_TYPES = "org.techytax.domain.BookValue.FOR_YEAR_AND_TYPES";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	protected Long id = 0L;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Enumerated(EnumType.ORDINAL)
	private BalanceType balanceType;

	private int bookYear;

	@Column(precision = 10)
	private BigInteger saldo;

	public BookValue() {
	}

	public BookValue(BalanceType balanceType, int bookYear, BigInteger saldo) {
		this.balanceType = balanceType;
		this.bookYear = bookYear;
		this.saldo = saldo;
	}

	public String getDescription() {
		return balanceType.getKey();
	}

    @Override
    public String toString() {
        return balanceType + "," + bookYear + "," + saldo;
    }
}
