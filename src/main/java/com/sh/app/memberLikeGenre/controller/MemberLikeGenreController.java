package com.sh.app.memberLikeGenre.controller;

import com.sh.app.memberLikeGenre.dto.MemberLikeGenreListDto;
import com.sh.app.memberLikeGenre.dto.MemberLikeGenreResponseDto;
import com.sh.app.memberLikeGenre.serviece.MemberLikeGenreServiece;
import com.sh.app.movie.dto.MovieDto;
import com.sh.app.movie.entity.Movie;
import com.sh.app.movie.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequestMapping("/")
public class MemberLikeGenreController {

    @Autowired
    private MemberLikeGenreServiece memberLikeGenreServiece;

    @Autowired
    private MovieService movieService;

    @GetMapping("GenerLike")
    public ResponseEntity<?> GenerLikeList(@RequestParam Long memberId, Model model) {

        try {
            MemberLikeGenreListDto memberLikeGenre = memberLikeGenreServiece.findMemberLikeGenreInfoByMemberId(memberId);
            log.debug("memberLikeGenre = {}", memberLikeGenre);

            // 가져온 선호 장르 ID를 사용하여 장르에 맞는 영화 목록을 가져옵니다.
            Long genreId = memberLikeGenre.getGenreId(); // 선호 장르 ID
            List<Movie> movies = movieService.getMoviesByGenre(genreId); // 장르 ID에 해당하는 영화 목록

            List<MovieDto> movieDtos = movies.stream().map(movie -> MovieDto.builder()
                    .id(movie.getId())
                    .title(movie.getTitle())
                    .posterUrl(movie.getPosterUrl())
                    .filmRatings(movie.getFilmRatings())
                    .voteAverage(movie.getVoteAverage())
                    .build()).collect(Collectors.toList());

            MemberLikeGenreResponseDto responseDto = MemberLikeGenreResponseDto.builder()
                    .memberLikeGenre(memberLikeGenre)
                    .movies(movieDtos)
                    .build();
            log.debug("responseDto = {}", responseDto);

            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

