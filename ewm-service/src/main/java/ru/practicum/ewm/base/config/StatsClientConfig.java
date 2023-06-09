package ru.practicum.ewm.base.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.StatClient;

@Configuration
public class StatsClientConfig {

    @Value("${stats-service.url}")
    private String url;

    @Bean
    StatClient statClient() {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        return new StatClient(url, builder);
    }
}
