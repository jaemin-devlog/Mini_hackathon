package org.likelion.hsu.mini_hsu.Repository;

import org.likelion.hsu.mini_hsu.Entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    // title에 키워드 포함된 데이터 조회 (대소문자 구분 없는 like 검색)
    List<Movie> findByTitleContainingIgnoreCase(String keyword);

    // title 또는 content에 키워드 포함된 데이터 조회 (JPQL @Query 사용)
    @Query("SELECT m FROM Movie m WHERE LOWER(m.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(m.content) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Movie> searchByTitleOrContent(@Param("keyword") String keyword);

}
