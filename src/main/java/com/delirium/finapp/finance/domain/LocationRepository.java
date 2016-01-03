package com.delirium.finapp.finance.domain;

import com.delirium.finapp.groups.domain.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by style on 03/01/2016.
 */
public interface LocationRepository extends JpaRepository<Location, Long> {

}

