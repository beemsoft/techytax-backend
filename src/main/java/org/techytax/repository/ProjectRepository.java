package org.techytax.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.techytax.domain.Project;

import org.techytax.model.security.User;

import java.util.Collection;

public interface ProjectRepository extends CrudRepository<Project, Long> {

    Collection<Project> findByUser(User user);

    @Query("select p from Project p where p.user = ?1 and p.endDate is null")
    Collection<Project> findCurrentProjects(User user);

    @Modifying
    @Query("delete from Project p where p.user = ?1")
    void deleteProjectsByUser(User user);
}
