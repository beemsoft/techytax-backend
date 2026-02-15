package org.techytax.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.techytax.domain.Activum;
import org.techytax.domain.BalanceType;
import org.techytax.domain.BusinessCar;
import org.techytax.domain.Office;

import org.techytax.model.security.User;

import java.time.LocalDate;
import java.util.Collection;

public interface ActivumRepository extends CrudRepository<Activum, Long> {

    @Query("select a from Activum a " +
            "where a.user = ?1 " +
            "order by a.endDate asc, a.startDate desc")
    Collection<Activum> findByUser(User user);

    @Query("select a from Activum a " +
      "where a.user = ?1 and a.balanceType = ?2 and (a.endDate is null or a.endDate between ?3 and ?4)")
    Collection<Activum> findActivums(User user, BalanceType balanceType, LocalDate startDate, LocalDate endDate);

    @Query("select a from Activum a " +
      "where a.user = ?1 and a.balanceType = org.techytax.domain.BalanceType.CAR and (a.endDate is null or a.endDate between ?2 and ?3)")
    Collection<BusinessCar> findBusinessCars(User user, LocalDate startDate, LocalDate endDate);

    @Query("select a from Activum a where a.user = ?1 and a.balanceType = org.techytax.domain.BalanceType.OFFICE")
    Office findOffice(User user);

    @Modifying
    @Query("delete from Activum a where a.user = ?1")
    void deleteActivumsByUser(User user);
}
