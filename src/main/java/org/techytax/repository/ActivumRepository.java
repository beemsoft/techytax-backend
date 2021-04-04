package org.techytax.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.techytax.domain.Activum;
import org.techytax.domain.BalanceType;
import org.techytax.domain.BusinessCar;
import org.techytax.domain.Office;

import java.time.LocalDate;
import java.util.Collection;

public interface ActivumRepository extends CrudRepository<Activum, Long> {

    Collection<Activum> findByUser(String username);

    @Query("select a from Activum a " +
      "where a.user = ?1 and a.balanceType = ?2 and (a.endDate is null or a.endDate between ?3 and ?4)")
    Collection<Activum> findActivums(String username, BalanceType balanceType, LocalDate startDate, LocalDate endDate);

    @Query("select a from Activum a " +
      "where a.user = ?1 and a.balanceType = org.techytax.domain.BalanceType.CAR and (a.endDate is null or a.endDate between ?2 and ?3)")
    BusinessCar findBusinessCar(String username, LocalDate startDate, LocalDate endDate);

    @Query("select a from Activum a where a.user = ?1 and a.balanceType = org.techytax.domain.BalanceType.OFFICE")
    Office findOffice(String username);

    @Modifying
    @Query("delete from Activum a where a.user = ?1")
    void deleteActivumsByUser(String username);
}
