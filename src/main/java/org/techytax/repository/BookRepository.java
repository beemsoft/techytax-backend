package org.techytax.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.techytax.domain.BalanceType;
import org.techytax.domain.BookValue;

import org.techytax.model.security.User;

import java.util.Collection;

public interface BookRepository extends CrudRepository<BookValue, Long> {

    @Query("select b from BookValue as b where b.user = ?1 order by b.bookYear desc")
    Collection<BookValue> findByUser(User user);

    BookValue findBookValueByUserAndBalanceTypeAndBookYear(User user, BalanceType balanceType, int year);

    @Modifying
    @Query("delete from BookValue b where b.user = ?1")
    void deleteBookValues(User user);
}
