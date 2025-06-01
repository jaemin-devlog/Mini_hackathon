package org.likelion.hsu.mini_hsu.Service;

import lombok.RequiredArgsConstructor;
import org.likelion.hsu.mini_hsu.Dto.MovieRequestDto;
import org.likelion.hsu.mini_hsu.Entity.Movie;
import org.likelion.hsu.mini_hsu.Repository.MovieRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class MovieService {


    private final MovieRepository movieRepository;

    //리뷰 전체조회
    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    //리뷰 검색조회
    public List<Movie> searchMovies(String keyword) {
        return movieRepository.searchByTitleOrContent(keyword);
    }

    // 리뷰 생성 (Create)
    public Movie createMovie(String title, String content) {
        Movie movie = Movie.builder()
                .title(title)
                .content(content)
                .build();
        return movieRepository.save(movie);
    }

    //리뷰 삭제 (Delete)
    public void deleteMovie(Long id) {
        // 삭제할 리뷰 존재 여부 확인
        if (!movieRepository.existsById(id)) {
            throw new NoSuchElementException("삭제할 리뷰가 존재하지 않습니다.");
        }
        movieRepository.deleteById(id);
    }

    //리뷰 수정
    @Transactional
    public Movie update(Long id, MovieRequestDto dto) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 없습니다. id=" + id));

        movie.setTitle(dto.getTitle());
        movie.setContent(dto.getContent());
        return movie;
    }

    //좋아요 기능
    public Movie increaseLikeCount(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new NoSuchElementException("영화가 없습니다."));
        movie.setLikeCount(movie.getLikeCount() + 1);
        return movieRepository.save(movie);
    }

}
