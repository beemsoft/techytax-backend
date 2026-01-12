package org.techytax.saas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.techytax.saas.domain.Registration;

import org.techytax.model.security.User;

import java.util.Collection;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    Collection<Registration> findByUser(User user);

    @Modifying
    @Query("delete from Registration r where r.user = ?1")
    void deleteRegistrationByUser(User user);
}
