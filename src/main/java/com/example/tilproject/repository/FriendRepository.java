package com.example.tilproject.repository;

import com.example.tilproject.entity.Friendship;
import com.example.tilproject.entity.User;
import com.example.tilproject.enums.FriendshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friendship, Long> {
    boolean existsByFromUserAndToUser(User fromUser, User toUser);

    // 친구 관계 존재 여부 (수락 상태)
    @Query("""
        SELECT CASE WHEN COUNT(f) > 0 THEN TRUE ELSE FALSE END
        FROM Friendship f
        WHERE ((f.fromUser.userId = :user1 AND f.toUser.userId = :user2)
            OR (f.fromUser.userId = :user2 AND f.toUser.userId = :user1))
        AND f.status = 'ACCEPTED'
    """)
    // 내가 보낸 요청 중 특정 상태
    List<Friendship> findByFromUserAndStatus(User fromUser, FriendshipStatus status);

    // 내가 받은 요청 중 특정 상태
    List<Friendship> findByToUserAndStatus(User toUser, FriendshipStatus status);

    // 친구 목록 조회 (양방향)
    @Query("""
        SELECT f.toUser FROM Friendship f
        WHERE f.fromUser.userId = :userId AND f.status = 'ACCEPTED'
        UNION
        SELECT f.fromUser FROM Friendship f
        WHERE f.toUser.userId = :userId AND f.status = 'ACCEPTED'
    """)
    List<User> findAcceptedFriends(@Param("userId") Long userId);

    @Query("""
    SELECT f FROM Friendship f
    WHERE ((f.fromUser.userId = :user1 AND f.toUser.userId = :user2)
       OR  (f.fromUser.userId = :user2 AND f.toUser.userId = :user1))
""")
    Optional<Friendship> findByUserIdsBidirectional(@Param("user1") Long user1, @Param("user2") Long user2);

    @Query("SELECT COUNT(f) > 0 FROM Friendship f " +
            "WHERE ((f.fromUser.userId = :user1 AND f.toUser.userId = :user2) " +
            "   OR (f.fromUser.userId = :user2 AND f.toUser.userId = :user1)) " +
            "AND f.status = 'ACCEPTED'")
    boolean isFriend(@Param("user1") Long user1, @Param("user2") Long user2);

}
