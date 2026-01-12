package org.techytax.domain;

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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@Entity
@NamedQueries({
		@NamedQuery(name = BookValue.HISTORY, query = "SELECT bv FROM BookValue bv WHERE bv.user = :user order by bv.balanceType asc, bv.bookYear desc"),
		@NamedQuery(name = BookValue.FOR_YEAR, query = "SELECT bv FROM BookValue bv WHERE bv.user = :user AND bv.bookYear = :bookYear order by bv.balanceType asc"),
		@NamedQuery(name = BookValue.GET, query = "SELECT bv FROM BookValue bv WHERE bv.bookYear = :bookYear and bv.user = :user and bv.balanceType = :balanceType"),
		@NamedQuery(name = BookValue.FOR_YEAR_AND_TYPES, query = "SELECT bv FROM BookValue bv WHERE bv.user = :user AND bv.bookYear = :bookYear AND bv.balanceType IN :balanceTypes ORDER BY bv.balanceType asc") })
@Table(name = "bookvalue")
@Getter
@Setter
public class BookValue {

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

	public BalanceType getBalanceType() {
		return balanceType;
	}

	public void setBalanceType(BalanceType balanceType) {
		this.balanceType = balanceType;
	}

	public int getBookYear() {
		return bookYear;
	}

	public void setBookYear(int bookYear) {
		this.bookYear = bookYear;
	}

	public BigInteger getSaldo() {
		return saldo;
	}

	public void setSaldo(BigInteger saldo) {
		this.saldo = saldo;
	}

	static final String HISTORY = "org.techytax.domain.BookValue.HISTORY";
	static final String FOR_YEAR = "org.techytax.domain.BookValue.FOR_YEAR";
	public static final String GET = "org.techytax.domain.BookValue.GET";
	static final String FOR_YEAR_AND_TYPES = "org.techytax.domain.BookValue.FOR_YEAR_AND_TYPES";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
