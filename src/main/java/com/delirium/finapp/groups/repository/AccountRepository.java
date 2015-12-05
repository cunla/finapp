package com.delirium.finapp.groups.repository;

import com.delirium.finapp.groups.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the User entity.
 */
public interface AccountRepository extends JpaRepository<Group, Long> {
    Group findOneByName(String name);
}
