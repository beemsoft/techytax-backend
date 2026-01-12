package org.techytax.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.techytax.domain.Cost;
import org.techytax.domain.CostType;

import org.techytax.model.security.User;

import java.time.LocalDate;
import java.util.Collection;

public interface CostRepository extends CrudRepository<Cost, Long> {

    @Query("select c from Cost c " +
            "where c.user = ?1 order by c.date desc")
    Collection<Cost> findByUser(User user);

    @Query("select c from Cost c " +
      "where c.user = ?1 and c.costType = ?2 and c.date between ?3 and ?4 and c.amount is not null and c.amount > 0")
    Collection<Cost> findCosts(User user, CostType costType, LocalDate fromDate, LocalDate toDate);

    @Modifying
    @Query("delete from Cost c where c.user = ?1")
    void deleteCostsByUser(User user);
}
