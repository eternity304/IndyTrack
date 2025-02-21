package com.inde.indytrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.inde.indytrack.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT * FROM users WHERE email = :email", nativeQuery = true)
    Optional<User> findByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM users WHERE first_name like :firstName AND last_name like :lastName", nativeQuery = true)
    Optional<User> findByFullName(@Param("firstName") String firstName, @Param("lastName") String lastName);
    
}
