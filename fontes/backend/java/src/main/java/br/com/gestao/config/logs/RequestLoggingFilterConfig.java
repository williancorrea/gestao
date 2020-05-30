package br.com.gestao.config.logs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RequestLoggingFilterConfig {

    @Bean
    public CustomizedRequestLoggingFilter filter() {
        return new CustomizedRequestLoggingFilter();
    }
}
