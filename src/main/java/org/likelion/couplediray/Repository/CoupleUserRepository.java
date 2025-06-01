package org.likelion.couplediray.Repository;

import org.likelion.couplediray.Entity.Couple;
import org.likelion.couplediray.Entity.CoupleUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.likelion.couplediray.Entity.User;
import java.util.List;

public interface CoupleUserRepository extends JpaRepository<CoupleUser, Long> {
    @Query("SELECT cu FROM CoupleUser cu WHERE cu.user.userId = :userId")
    List<CoupleUser> findByUserId(@Param("userId") Long userId);
    List<CoupleUser> findAllByCouple(Couple couple);
    List<CoupleUser> findByCouple(Couple couple);
    // 내가 속한 커플 정보 찾기
    List<CoupleUser> findByUser(User user);
}
