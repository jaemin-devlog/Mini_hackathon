package com.example.running.repository;

import com.example.running.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.badges WHERE u.userId = :userId")
    Optional<User> findByIdWithBadges(@Param("userId") Long userId);
}
