package com.gugucon.shopping.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class AsyncEventConfig {

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor asyncExecutor = new ThreadPoolTaskExecutor();
        asyncExecutor.setThreadNamePrefix("async-pool");
        asyncExecutor.setCorePoolSize(3);
        asyncExecutor.setMaxPoolSize(10);
        asyncExecutor.initialize();
        return asyncExecutor;
    }
}
