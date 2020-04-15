package org.techytax.domain.fiscal;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.techytax.domain.Activum;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;

@Data
public class VatReport {

    @JsonFormat(
      pattern="yyyy-MM-dd",
      shape=JsonFormat.Shape.STRING)
    private LocalDate latestTransactionDate;
    private BigDecimal totalCarCosts;
    private BigDecimal totalTransportCosts;
    private BigDecimal totalOfficeCosts;
    private BigDecimal totalFoodCosts;
    private BigDecimal totalOtherCosts;
    private BigDecimal totalVatIn;
    private BigDecimal totalVatOut;
    private BigDecimal sentInvoices;
    private BigDecimal vatSaldo;
    private BigInteger vatCorrectionForPrivateUsage;
    private ArrayList<Activum> investments;
}
