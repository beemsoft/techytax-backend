package org.techytax.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.techytax.domain.Activity;

import java.time.LocalDate;
import java.util.Collection;

public interface ActivityRepository extends CrudRepository<Activity, Long> {

    Collection<Activity> findByUser(String username);

    @Modifying
    @Query("delete from Activity a where a.user = ?1")
    void deleteActivitiesByUser(String username);

    @Query("select a from Activity a " +
            "where a.user = ?1 and a.project.id = ?2 and a.activityDate between ?3 and ?4")
    Collection<Activity> getActivitiesForProject(String username, Long projectId, LocalDate fromDate, LocalDate toDate);
}
