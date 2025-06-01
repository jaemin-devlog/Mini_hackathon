package org.likelion.hsu.mini_hsu.Controller;

import lombok.RequiredArgsConstructor;
import org.likelion.hsu.mini_hsu.Dto.MovieRequestDto;
import org.likelion.hsu.mini_hsu.Dto.MovieResponseDto;
import org.likelion.hsu.mini_hsu.Entity.Movie;
import org.likelion.hsu.mini_hsu.Service.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/movie")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    //댓글 생성
    @PostMapping
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movieRequest) {
        Movie savedMovie = movieService.createMovie(movieRequest.getTitle(), movieRequest.getContent());
        return new ResponseEntity<>(savedMovie, HttpStatus.CREATED);
    }

    //댓글삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.ok("리뷰 삭제가 완료되었습니다.");
    }

    //전체댓글조회
    @GetMapping("/view")
    public ResponseEntity<List<MovieResponseDto>> getMovies() {
        List<MovieResponseDto> allMovies = movieService.findAll()
                .stream()
                .map(MovieResponseDto::new)
                .toList();

        return ResponseEntity.ok(allMovies);
    }

    //리뷰 검색 조회
    @GetMapping("/search")
    public ResponseEntity<List<MovieResponseDto>> searchMovies(@RequestParam String keyword) {
        List<Movie> movies = movieService.searchMovies(keyword);
        List<MovieResponseDto> result = movies.stream()
                .map(MovieResponseDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    //댓글 수정
    @PutMapping("/{id}")
    public ResponseEntity<MovieResponseDto> updateMovie(
            @PathVariable Long id,
            @RequestBody MovieRequestDto requestDto) {

        Movie updatedMovie = movieService.update(id, requestDto);
        MovieResponseDto responseDto = new MovieResponseDto(updatedMovie);
        return ResponseEntity.ok(responseDto);
    }

    //좋아요 기능
    @PostMapping("/{id}/like")
    public ResponseEntity<Map<String, Object>> likeMovie(@PathVariable Long id) {
        Movie movie = movieService.increaseLikeCount(id);
        Map<String, Object> response = new HashMap<>();
        response.put("likeCount", movie.getLikeCount());
        return ResponseEntity.ok(response);
    }
}
