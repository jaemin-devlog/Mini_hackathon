package org.likelion.couplediray.Repository;

import org.likelion.couplediray.Entity.Couple;
import org.likelion.couplediray.Entity.CoupleUser;
import org.likelion.couplediray.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoupleUserRepository extends JpaRepository<CoupleUser, Long> {
    List<CoupleUser> findByUser(User user);
    List<CoupleUser> findAllByCouple(Couple couple);
}
