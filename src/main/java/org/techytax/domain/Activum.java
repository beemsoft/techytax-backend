package org.techytax.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.techytax.helper.DepreciationHelper;

import org.techytax.model.security.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

@Entity
@Table(name = "activa")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public class Activum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id = 0L;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String description;

    @Column(precision = 10, scale = 2)
    private BigDecimal purchasePrice;

    @Column(precision = 10, scale = 2)
    private BigInteger remainingValue;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate purchaseDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Enumerated(EnumType.ORDINAL)
    private BalanceType balanceType;

    public int getNofYearsForDepreciation() {
        return nofYearsForDepreciation;
    }

    public void setNofYearsForDepreciation(int nofYearsForDepreciation) {
        this.nofYearsForDepreciation = nofYearsForDepreciation;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public BigInteger getRemainingValue() {
        return remainingValue;
    }

    public void setRemainingValue(BigInteger remainingValue) {
        this.remainingValue = remainingValue;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BalanceType getBalanceType() {
        return balanceType;
    }

    public void setBalanceType(BalanceType balanceType) {
        this.balanceType = balanceType;
    }

    private int nofYearsForDepreciation;

    public String getOmschrijving() {
        if (balanceType != null)
            return balanceType.getKey();
        else return "Undefined type";
    }

    public BigInteger getDepreciation() {
        if (remainingValue == null) return BigInteger.ZERO;
        return DepreciationHelper.getDepreciation(this);
    }

    public BigInteger getValueAtEndOfFiscalYear() {
        if (remainingValue == null) return BigInteger.ZERO;
        return DepreciationHelper.getValueAtEndOfFiscalYear(this);
    }

}
