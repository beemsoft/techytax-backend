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

    public LocalDate getLatestTransactionDate() {
        return latestTransactionDate;
    }

    public void setLatestTransactionDate(LocalDate latestTransactionDate) {
        this.latestTransactionDate = latestTransactionDate;
    }

    public BigDecimal getTotalCarCosts() {
        return totalCarCosts;
    }

    public void setTotalCarCosts(BigDecimal totalCarCosts) {
        this.totalCarCosts = totalCarCosts;
    }

    public BigDecimal getTotalTransportCosts() {
        return totalTransportCosts;
    }

    public void setTotalTransportCosts(BigDecimal totalTransportCosts) {
        this.totalTransportCosts = totalTransportCosts;
    }

    public BigDecimal getTotalOfficeCosts() {
        return totalOfficeCosts;
    }

    public void setTotalOfficeCosts(BigDecimal totalOfficeCosts) {
        this.totalOfficeCosts = totalOfficeCosts;
    }

    public BigDecimal getTotalFoodCosts() {
        return totalFoodCosts;
    }

    public void setTotalFoodCosts(BigDecimal totalFoodCosts) {
        this.totalFoodCosts = totalFoodCosts;
    }

    public BigDecimal getTotalOtherCosts() {
        return totalOtherCosts;
    }

    public void setTotalOtherCosts(BigDecimal totalOtherCosts) {
        this.totalOtherCosts = totalOtherCosts;
    }

    public BigDecimal getTotalVatIn() {
        return totalVatIn;
    }

    public void setTotalVatIn(BigDecimal totalVatIn) {
        this.totalVatIn = totalVatIn;
    }

    public BigDecimal getTotalVatOut() {
        return totalVatOut;
    }

    public void setTotalVatOut(BigDecimal totalVatOut) {
        this.totalVatOut = totalVatOut;
    }

    public BigDecimal getSentInvoices() {
        return sentInvoices;
    }

    public void setSentInvoices(BigDecimal sentInvoices) {
        this.sentInvoices = sentInvoices;
    }

    public BigDecimal getVatSaldo() {
        return vatSaldo;
    }

    public void setVatSaldo(BigDecimal vatSaldo) {
        this.vatSaldo = vatSaldo;
    }

    public BigInteger getVatCorrectionForPrivateUsage() {
        return vatCorrectionForPrivateUsage;
    }

    public void setVatCorrectionForPrivateUsage(BigInteger vatCorrectionForPrivateUsage) {
        this.vatCorrectionForPrivateUsage = vatCorrectionForPrivateUsage;
    }

    public ArrayList<Activum> getInvestments() {
        return investments;
    }

    public void setInvestments(ArrayList<Activum> investments) {
        this.investments = investments;
    }

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
