package org.techytax.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Getter
@Setter
public class Activity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id = 0L;

	@NotNull
	private String user;

	@NotNull
	@ManyToOne
	private Project project;

	@Column(precision=2, scale=2)
	private BigDecimal hours;

	private String activityDescription;

	private Date activityDate;

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Activity)) {
			return false;
		}
		Activity other = (Activity)obj;
		return this.id.equals(other.id);
	}
}
