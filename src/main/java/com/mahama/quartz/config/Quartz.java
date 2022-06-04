package com.mahama.quartz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class Quartz {

    @Bean(name = "scheduler")
    public SchedulerFactoryBean schedulerFactory() {
        return new SchedulerFactoryBean();
    }
}
