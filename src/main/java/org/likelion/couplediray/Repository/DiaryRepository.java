package org.likelion.couplediray.Repository;

import org.likelion.couplediray.Entity.Couple;
import org.likelion.couplediray.Entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    Optional<Diary> findByCoupleAndDate(Couple couple, LocalDate date);
    void deleteByCoupleAndDate(Couple couple, LocalDate date);
}