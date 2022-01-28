package me.doyoung.springcorehigh;

import me.doyoung.springcorehigh.trace.logtrace.FiledLogTrace;
import me.doyoung.springcorehigh.trace.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogTraceConfig {
    @Bean
    public LogTrace logTrace() {
        return new FiledLogTrace();
    }
}
