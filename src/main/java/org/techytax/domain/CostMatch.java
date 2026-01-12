package org.techytax.domain;

import lombok.Data;

import org.techytax.model.security.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Data
public class CostMatch {

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

    public String getMatchString() {
        return matchString;
    }

    public void setMatchString(String matchString) {
        this.matchString = matchString;
    }

    public CostType getCostType() {
        return costType;
    }

    public void setCostType(CostType costType) {
        this.costType = costType;
    }

    public int getCostCharacter() {
        return costCharacter;
    }

    public void setCostCharacter(int costCharacter) {
        this.costCharacter = costCharacter;
    }

    public VatType getVatType() {
        return vatType;
    }

    public void setVatType(VatType vatType) {
        this.vatType = vatType;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public int getFixedAmount() {
        return fixedAmount;
    }

    public void setFixedAmount(int fixedAmount) {
        this.fixedAmount = fixedAmount;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id = 0L;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @Size(min = 2, max = 50)
    private String matchString;

    @ManyToOne
    private CostType costType;

    private int costCharacter;

    @Enumerated(EnumType.ORDINAL)
    private VatType vatType;

    private int percentage;
    private int fixedAmount;

}
