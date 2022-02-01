package me.doyoung.springcorehigh;

import me.doyoung.springcorehigh.trace.logtrace.LogTrace;
import me.doyoung.springcorehigh.trace.logtrace.ThreadLocalLogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogTraceConfig {
    @Bean
    public LogTrace logTrace() {
        return new ThreadLocalLogTrace();
    }
}
