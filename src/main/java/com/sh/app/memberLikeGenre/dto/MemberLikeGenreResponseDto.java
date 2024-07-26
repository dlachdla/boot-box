package com.sh.app.memberLikeGenre.dto;

import com.sh.app.movie.dto.MovieDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberLikeGenreResponseDto {
    private MemberLikeGenreListDto memberLikeGenre;
    private List<MovieDto> movies;
}
