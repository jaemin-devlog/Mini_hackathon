package org.likelion.couplediray.Service;

import lombok.RequiredArgsConstructor;
import org.likelion.couplediray.Entity.Couple;
import org.likelion.couplediray.Entity.Diary;
import org.likelion.couplediray.Entity.User;
import org.likelion.couplediray.Repository.CoupleUserRepository;
import org.likelion.couplediray.Repository.DiaryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final CoupleUserRepository coupleUserRepository;

    public String getDiary(User user, LocalDate date) {
        Couple couple = coupleUserRepository.findByUser(user).get(0).getCouple();
        return diaryRepository.findByCoupleAndDate(couple, date)
                .map(Diary::getContent)
                .orElse("");
    }

    public void saveDiary(User user, LocalDate date, String content) {
        Couple couple = coupleUserRepository.findByUser(user).get(0).getCouple();

        Diary diary = diaryRepository.findByCoupleAndDate(couple, date)
                .orElseGet(() -> {
                    Diary newDiary = new Diary();
                    newDiary.setDate(date);
                    newDiary.setCouple(couple);
                    return newDiary;
                });

        diary.setContent(content);
        diaryRepository.save(diary);
    }

    public void deleteDiary(User user, LocalDate date) {
        Couple couple = coupleUserRepository.findByUser(user).get(0).getCouple();
        diaryRepository.deleteByCoupleAndDate(couple, date);
    }
}