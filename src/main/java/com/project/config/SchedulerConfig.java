package com.project.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.TaskScheduler;

/**
 * Configuration class for setting up a TaskScheduler bean.
 * This configuration enables scheduling tasks with a thread pool
 * to manage concurrent task execution efficiently.
 */
@Configuration
public class SchedulerConfig {

	/**
     * Defines a TaskScheduler bean.
     * The ThreadPoolTaskScheduler is used to schedule and manage tasks with a configurable pool size.
     *
     * @return a configured instance of TaskScheduler
     */
    @Bean
    public TaskScheduler taskScheduler() {
    	 // Create a new ThreadPoolTaskScheduler instance
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        // Set the size of the thread pool to handle concurrent tasks
        taskScheduler.setPoolSize(10);
        // Set a custom thread name prefix for easier debugging and identification of threads
        taskScheduler.setThreadNamePrefix("OrderScheduler-");
        // Return the configured TaskScheduler instance
        return taskScheduler;
    }
}

