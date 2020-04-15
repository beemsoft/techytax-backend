package org.techytax.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.techytax.helper.DepreciationHelper;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
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
    private String user;

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

    private int nofYearsForDepreciation;

    public String getOmschrijving() {
        return balanceType.getKey();
    }

    public BigInteger getDepreciation() {
        return DepreciationHelper.getDepreciation(this);
    }

    public BigInteger getValueAtEndOfFiscalYear() {
        return DepreciationHelper.getValueAtEndOfFiscalYear(this);
    }

}
