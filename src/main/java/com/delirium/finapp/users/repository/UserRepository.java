package com.delirium.finapp.users.repository;

import com.delirium.finapp.users.domain.User;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the User entity.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.activationKey = ?1")
    User getUserByActivationKey(
        String activationKey);

    @Query("select u from User u where u.activated = false and u.createdDate > ?1")
    List<User> findNotActivatedUsersByCreationDateBefore(DateTime dateTime);

    @Query("select u from User u")
    List<User> findAll();

    User findOneByEmail(String email);

    @Query("select u from User u where u.name like %:q% or u.email like %:q%")
    List<User> findAllContaining(@Param("q") String query);

}
