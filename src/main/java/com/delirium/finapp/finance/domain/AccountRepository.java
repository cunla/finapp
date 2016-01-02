package com.delirium.finapp.finance.domain;

import com.delirium.finapp.groups.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
//    Group findOneByName(String name);

    @Query("SELECT c from Account c where c.group=:group")
    public List<Account> accountsForGroup(@Param("group") Group group);
}
