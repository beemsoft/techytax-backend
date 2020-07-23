package org.techytax.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.techytax.domain.Activity;

import java.util.Collection;

public interface ActivityRepository extends CrudRepository<Activity, Long> {

    Collection<Activity> findByUser(String username);

    @Modifying
    @Query("delete from Activity p where p.user = ?1")
    void deleteActivitiesByUser(String username);
}
