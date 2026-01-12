package org.techytax.domain.fiscal;

import lombok.Data;

import org.techytax.model.security.User;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

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
