package com.studentmanagement.student_management.repository;

import com.studentmanagement.student_management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @org.springframework.data.jpa.repository.Query(value = "SELECT * FROM app_users WHERE username = :username", nativeQuery = true)
    Optional<User> findByUsername(@org.springframework.data.repository.query.Param("username") String username);
    
    @org.springframework.data.jpa.repository.Query(value = "SELECT CASE WHEN COUNT(id) > 0 THEN true ELSE false END FROM app_users WHERE username = :username", nativeQuery = true)
    Boolean existsByUsername(@org.springframework.data.repository.query.Param("username") String username);
}
