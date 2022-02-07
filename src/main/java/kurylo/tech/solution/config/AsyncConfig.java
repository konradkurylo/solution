package kurylo.tech.solution.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableAsync
class AsyncConfig {

    @Bean
    public Executor taskExecutor() {
        return Executors.newFixedThreadPool(100);
    }
}
