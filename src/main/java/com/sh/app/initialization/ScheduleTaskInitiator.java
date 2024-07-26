package com.sh.app.initialization;

import com.sh.app.movie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

@Component
public class ScheduleTaskInitiator implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private MovieService movieService;

    @Autowired
    @Qualifier("taskScheduler")
    private TaskScheduler taskScheduler;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        taskScheduler.schedule(this::scheduledTask, new CronTrigger("0 0 3 * * *"));
        taskScheduler.schedule(this::calculateReviewRatings, new CronTrigger("0 0 2 * * *"));
    }

    public void scheduledTask() {
        movieService.scheduledCallApi();
    }

    public void calculateReviewRatings() {
        movieService.updateMovieRatings();
    }
}