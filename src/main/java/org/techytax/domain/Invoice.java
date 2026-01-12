package org.techytax.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.Data;

import org.techytax.model.security.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Getter
@Setter
public class Invoice {

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public float getUnitsOfWork() {
        return unitsOfWork;
    }

    public void setUnitsOfWork(float unitsOfWork) {
        this.unitsOfWork = unitsOfWork;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }

    public String getOriginalInvoiceNumber() {
        return originalInvoiceNumber;
    }

    public void setOriginalInvoiceNumber(String originalInvoiceNumber) {
        this.originalInvoiceNumber = originalInvoiceNumber;
    }

    public int getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(int discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public LocalDate getSent() {
        return sent;
    }

    public void setSent(LocalDate sent) {
        this.sent = sent;
    }

    public LocalDate getPaid() {
        return paid;
    }

    public void setPaid(LocalDate paid) {
        this.paid = paid;
    }

    public String getHtmlText() {
        return htmlText;
    }

    public void setHtmlText(String htmlText) {
        this.htmlText = htmlText;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id = 0L;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

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
