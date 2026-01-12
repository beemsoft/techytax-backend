package org.techytax.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.techytax.model.security.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    @Modifying
    @Query("delete from User u where u.id = ?1")
    void deleteUser(Long id);
}
