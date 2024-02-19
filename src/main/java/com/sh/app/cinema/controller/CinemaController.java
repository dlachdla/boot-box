package com.sh.app.cinema.controller;

import com.sh.app.cinema.dto.CinemaDto;

import com.sh.app.cinema.service.CinemaService;
import com.sh.app.location.dto.LocationDto;
import com.sh.app.location.service.LocationService;
import com.sh.app.movie.dto.MovieListDto;
import com.sh.app.movie.service.MovieService;
import com.sh.app.schedule.dto.IScheduleInfoDto;
import com.sh.app.schedule.dto.ScheduleInfoDto;
import com.sh.app.schedule.dto.TheaterScheduleDto;
import com.sh.app.schedule.dto.TimeSlotDto;
import com.sh.app.schedule.service.ScheduleService;
import com.sh.app.seat.service.SeatService;
import com.sh.app.theater.service.TheaterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.stream.Collectors;

@Controller
@Slf4j
@RequestMapping("/cinema")
public class CinemaController {

    @Autowired
    private CinemaService cinemaService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/cinemaList.do")
    public String cinemaList(Model model) {
        List<LocationDto> locationsWithCinemas = locationService.findAllLocationsWithCinemas();
        model.addAttribute("locations", locationsWithCinemas);
        return "cinema/cinemaList"; // 해당하는 Thymeleaf 템플릿 이름
    }

    @GetMapping("/cinemaDetail.do")
    public String cinemaDetail(@RequestParam("id") Long id, Model model) {
        CinemaDto cinemaDto = cinemaService.getCinemaDetails(id);
        List<MovieListDto> currentMovies = movieService.getCurrentMovies(); // 현재 상영 중인 영화 목록 가져오기
        model.addAttribute("cinema", cinemaDto);
        model.addAttribute("currentMovies", currentMovies); // 모델에 추가
        return "cinema/cinemaDetail";
    }

    @GetMapping("/scheduleByDate")
    public ResponseEntity<?> getScheduleByDate(@RequestParam("id") Long id,
                                               @RequestParam("selectedDate") @DateTimeFormat(pattern = "yyyy/MM/dd") LocalDate selectedDate) {

        log.debug("id = {}", id); // 극장 ID
        log.debug("selectedDate = {}", selectedDate); // 선택된날짜
        List<IScheduleInfoDto> scheduleDetails = scheduleService.findScheduleDetailsByDateAndCinemaId(id, selectedDate);
        log.debug("scheduleDetails = {}", scheduleDetails);

        // 로직을 추가하여 scheduleDetails에서 필요한 JSON 구조로 변환
        List<Map<String, Object>> organizedSchedules = organizeSchedules(scheduleDetails);
        log.debug("organizedSchedules = {}", organizedSchedules);

        // JSON 구조로 클라이언트에 반환
        return ResponseEntity.ok(organizedSchedules);
    }

    private List<Map<String, Object>> organizeSchedules(List<IScheduleInfoDto> scheduleDetails) {
        // 영화별, 상영관별, 스케줄별 그룹화하기위한 맵
        Map<String, Map<String, List<Map<String, Object>>>> organized = new HashMap<>();
        // 각 영화별 상영 시간을 저장하기 위한 맵
        Map<String, String> movieDurations = new HashMap<>();

        for (IScheduleInfoDto dto : scheduleDetails) {
            String title = dto.getMovieTitle();
            String theater = dto.getTheaterName();
            String runningTime = dto.getRunningTime();

            movieDurations.putIfAbsent(title, runningTime); // 영화 제목에 해당하는 상영 시간을 맵에 저장

            // 영화 시간, 남은 좌석 그룹화
            Map<String, Object> timeMap = new HashMap<>();
            timeMap.put("time", dto.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")));
            timeMap.put("seatsAvailable", dto.getRemainingSeats());

            // 영화 제목별, 상영관별, 스케줄별에 따라 그룹화
            organized.computeIfAbsent(title, k -> new HashMap<>())
                    .computeIfAbsent(theater, k -> new ArrayList<>())
                    .add(timeMap);
        }

        // 데이터 가공하여 최종적으로 필요한 상영일정 구조로 변환
        List<Map<String, Object>> finalStructure = new ArrayList<>();
        organized.forEach((movieTitle, theaters) -> {
            // 영화 제목별 그룹화
            Map<String, Object> movieMap = new HashMap<>();
            movieMap.put("title", movieTitle);
            movieMap.put("totalDuration", movieDurations.get(movieTitle)); // 영화 제목에 해당하는 상영 시간을 사용

            // 상영관별 스케줄 그룹화
            List<Map<String, Object>> theaterList = new ArrayList<>();
            theaters.forEach((theaterName, timesList) -> {
                Map<String, Object> theaterMap = new HashMap<>();
                theaterMap.put("theater", theaterName);
                theaterMap.put("times", timesList);
                theaterList.add(theaterMap);
            });

            movieMap.put("schedules", theaterList);
            finalStructure.add(movieMap);
        });

        return finalStructure;
    }

}
