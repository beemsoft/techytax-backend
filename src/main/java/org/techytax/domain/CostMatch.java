package org.techytax.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.techytax.model.security.User;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CostMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
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
