package com.delirium.finapp.groups.domain;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the User entity.
 */
public interface GroupRepository extends JpaRepository<Group, Long> {
    Group findOneByName(String name);

//    @Query("SELECT g FROM Group g WHERE :user IN (g.users)")
//    List<Group> findAllForUser(@Param("user") User user);
}
