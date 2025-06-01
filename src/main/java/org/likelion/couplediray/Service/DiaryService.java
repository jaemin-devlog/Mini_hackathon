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
        Couple couple = coupleUserRepository.findByUserId(user.getUserId()).get(0).getCouple();
        return diaryRepository.findByCoupleAndDate(couple, date)
                .map(Diary::getContent)
                .orElse("");
    }

    public void saveDiary(User user, LocalDate date, String content) {
        Couple couple = coupleUserRepository.findByUserId(user.getUserId()).get(0).getCouple();

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
        Couple couple = coupleUserRepository.findByUserId(user.getUserId()).get(0).getCouple();
        diaryRepository.deleteByCoupleAndDate(couple, date);
    }

    // ✅ 메모 저장
    public void saveMemo(User user, LocalDate date, String memo) {
        Couple couple = coupleUserRepository.findByUserId(user.getUserId()).get(0).getCouple();

        Diary diary = diaryRepository.findByCoupleAndDate(couple, date)
                .orElseGet(() -> {
                    Diary newDiary = new Diary();
                    newDiary.setDate(date);
                    newDiary.setCouple(couple);
                    return newDiary;
                });

        diary.setMemo(memo);
        diaryRepository.save(diary);
    }

    // ✅ 메모 불러오기
    public String getMemo(User user, LocalDate date) {
        Couple couple = coupleUserRepository.findByUserId(user.getUserId()).get(0).getCouple();
        return diaryRepository.findByCoupleAndDate(couple, date)
                .map(Diary::getMemo)
                .orElse("");
    }
    // ✅ 버킷리스트 저장
    public void saveBucketList(User user, LocalDate date, String bucket) {
        Couple couple = coupleUserRepository.findByUserId(user.getUserId()).get(0).getCouple();

        Diary diary = diaryRepository.findByCoupleAndDate(couple, date)
                .orElseGet(() -> {
                    Diary newDiary = new Diary();
                    newDiary.setDate(date);
                    newDiary.setCouple(couple);
                    return newDiary;
                });

        diary.setBucketList(bucket); // Diary 엔티티에 필드 있어야 함
        diaryRepository.save(diary);
    }

    // ✅ 버킷리스트 불러오기
    public String getBucketList(User user, LocalDate date) {
        Couple couple = coupleUserRepository.findByUserId(user.getUserId()).get(0).getCouple();

        return diaryRepository.findByCoupleAndDate(couple, date)
                .map(Diary::getBucketList)  // Diary 엔티티에 getBucketList 있어야 함
                .orElse("");
    }
}
