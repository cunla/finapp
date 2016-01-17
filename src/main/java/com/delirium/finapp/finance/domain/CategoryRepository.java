package com.delirium.finapp.finance.domain;

import com.delirium.finapp.groups.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c from Category c where c.group=:group")
    public List<Category> categoryForGroup(@Param("group") Group group);

    @Query("select c from Category c where c.name=:name and c.group=:group")
    List<Category> findByName(@Param("group") Group group, @Param("name") String name);
}
