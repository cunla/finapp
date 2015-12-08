package com.delirium.finapp.groups.repository;

import com.delirium.finapp.groups.domain.Group;
import com.delirium.finapp.users.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the User entity.
 */
public interface GroupRepository extends JpaRepository<Group, Long> {
    Group findOneByName(String name);

    @Query("SELECT g FROM Group g WHERE :user IN g.users")
    List<Group> findAllForUser(@Param("user") User user);
}
