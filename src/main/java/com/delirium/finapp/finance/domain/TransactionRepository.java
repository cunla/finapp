package com.delirium.finapp.finance.domain;

import com.delirium.finapp.groups.domain.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

/**
 * Created by morand3 on 12/27/2015.
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.group=:group")
    Page<Transaction> findAllForGroup(@Param("group") Group group, Pageable pageable);

    @Query("SELECT t.amount from Transaction t where t.category=:category " +
        "and t.date >= :start and t.date <=:end")
    double totalForCategoryInPeriod(@Param("category") Category category,
                                    @Param("start") Date start,
                                    @Param("end") Date end);

    @Query("SELECT t.amount from Transaction t where t.account=:account")
    double totalForAcount(@Param("account") Account acc);
}
