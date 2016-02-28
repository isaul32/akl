package com.pyrenty.akl.component;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Component
public class SceduledTasks {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private TaskScheduler scheduler;

    @Scheduled(fixedRate=30000)
    public void getAllGamesToday() {
        System.out.println("Getting all games today");
        System.out.println("The time is now " + dateFormat.format(new Date()));
        scheduleGame();
    }

    Runnable gameRunnable = () -> {
        System.out.println("Game executing started");
        System.out.println("The time is now " + dateFormat.format(new Date()));
        };

    @Async
    public void scheduleGame() {
        ScheduledExecutorService localExecutor = Executors.newSingleThreadScheduledExecutor();
        scheduler = new ConcurrentTaskScheduler(localExecutor);
        scheduler.schedule(gameRunnable, new Date(System.currentTimeMillis() + 10000));
        System.out.println("Scheduled game");
    }
}
