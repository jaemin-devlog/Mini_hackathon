package com.example.tilproject.repository;

import com.example.tilproject.entity.TIL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TILRepository extends JpaRepository<TIL, Long> {

    List<TIL> findAllByUser_UserId(Long userId);
}
