package org.techytax.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id = 0L;

    @NotNull
    private String user;

    @NotNull
    @ManyToOne
    private Project project;

    @NotNull
    private String invoiceNumber;

    private String month;

    private float unitsOfWork;

    @Column(precision = 10, scale = 2)
    private BigDecimal revenue;

    @Transient
    private String originalInvoiceNumber;

    private int discountPercentage;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    private LocalDate sent;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    private LocalDate paid;

    private String htmlText;
}
