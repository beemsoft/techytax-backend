package org.techytax.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.techytax.domain.Invoice;

import org.techytax.model.security.User;

import java.time.LocalDate;
import java.util.Collection;

public interface InvoiceRepository extends CrudRepository<Invoice, Long> {

    Collection<Invoice> findByUser(User user);

    @Query("select i from Invoice i " +
      "where i.user = ?1 and i.sent between ?2 and ?3")
    Collection<Invoice> findInvoices(User user, LocalDate fromDate, LocalDate toDate);

    @Modifying
    @Query("delete from Invoice i where i.user = ?1")
    void deleteInvoicesByUser(User user);
}
