package com.github.dperezcabrera.bank.architecture.auth.repositories;

import com.github.dperezcabrera.bank.architecture.auth.entities.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    @Query("select u from User u where u.username = :username")
    Optional<User> findByUsername(@Param("username") String username);

    @Query("select count(u) > 0 from User u where u.username = :username and u.password = :password")
    boolean auth(@Param("username") String username, @Param("password") String password);
}
