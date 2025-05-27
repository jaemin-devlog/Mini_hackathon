package org.likelion.couplediray.Repository;

import org.likelion.couplediray.Entity.Couple;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoupleRepository extends JpaRepository<Couple, Long> {
    Optional<Couple> findByInviteCode(String inviteCode);
}