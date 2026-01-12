package org.techytax.domain.fiscal;

import lombok.Data;

import org.techytax.model.security.User;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Data
public class FiscalReport {

    @Id
    @GeneratedValue
    protected Long id = 0L;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    private Integer year;

    private FiscalOverview fiscalOverview;
}
