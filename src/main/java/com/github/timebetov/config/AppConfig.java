package com.github.timebetov.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@Slf4j
@Configuration
@EnableAspectJAutoProxy
public class AppConfig {

    @Bean("auditorAware")
    public AuditorAware<String> getCurrentAuditor() {
        return () -> Optional.of("SYSTEM");
    }
}
