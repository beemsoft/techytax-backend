package org.techytax.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.techytax.domain.CostMatch;

import org.techytax.model.security.User;

import java.util.Collection;

public interface CostMatchRepository extends CrudRepository<CostMatch, Long> {

    Collection<CostMatch> findByUser(User user);

    @Modifying
    @Query("delete from CostMatch c where c.user = ?1")
    void deleteCostMatchesByUser(User user);
}
