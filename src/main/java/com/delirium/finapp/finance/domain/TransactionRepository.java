package com.delirium.finapp.finance.domain;

import com.delirium.finapp.groups.domain.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Date;
import java.util.List;

/**
 * Created by morand3 on 12/27/2015.
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @RestResource(path = "groupTrans", rel = "groupTrans")
    @Query("SELECT t FROM com.delirium.finapp.finance.domain.Transaction t " +
        " WHERE t.group=:group " +
        " ORDER BY t.date DESC ")
    Page<Transaction> findAllForGroup(@Param("group") Group group, Pageable pageable);

    @Query("SELECT sum(t.amount) from Transaction t where t.category=:category " +
        "and t.date >= :start and t.date <=:end")
    Double totalForCategoryInPeriod(@Param("category") Category category,
                                    @Param("start") Date start,
                                    @Param("end") Date end);

    @Query("SELECT sum(t.amount) from Transaction t where t.account=:account")
    Double balanceForAccount(@Param("account") Account acc);

    @Query("SELECT sum(t.amount) from Transaction t where t.group=:groupi and t.account=null")
    Double balanceWithoutAccount(@Param("groupi") Group group);

    @Query("SELECT sum(t.amount) from Transaction t " +
        "where t.group=:groupi and t.category=null " +
        "and t.date >= :start and t.date <=:end")
    Double totalWithoutCategory(@Param("groupi") Group group,
                                @Param("start") Date start,
                                @Param("end") Date end);

    @Query("SELECT count(t.amount) from Transaction t where t.group=:groupi and t.account=null")
    Integer transactionsWithoutAcount(@Param("groupi") Group group);

    @Query("SELECT count(t.amount) from Transaction t " +
        "where t.group=:groupi and t.category=null " +
        "and t.date >= :start and t.date <=:end")
    Integer transactionsWithoutCategory(@Param("groupi") Group group, @Param("start") Date start, @Param("end") Date end);

    @Query("SELECT sum(t.amount) FROM Transaction t WHERE t.group=:group ")
    Double sumAllForGroup(@Param("group") Group group);

    @Query("SELECT count(t) FROM Transaction t " +
        " WHERE t.group=:group AND (t.category=null OR t.account=null)")
    Integer countTransactionsWithMissingData(@Param("group") Group group);

    @Query("SELECT t FROM com.delirium.finapp.finance.domain.Transaction t " +
        " WHERE t.group=:group " +
        " ORDER BY t.date DESC ")
    List<Transaction> findAllForGroup(@Param("group") Group group);
}
