package com.sixback.omesschat.common.config;

import com.sixback.omesschat.common.filter.CorsFilter;
import com.sixback.omesschat.common.filter.HttpCookieFilter;
import com.sixback.omesschat.common.filter.WebSocketUpgradeFilter;
import com.sixback.omesschat.domain.chat.service.WebSocketSessionService;
import com.sixback.omesschat.domain.session.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.web.server.WebFilter;

@Configuration
@RequiredArgsConstructor
public class WebConfig {

    private final DatabaseClient databaseClient;
    private final WebSocketSessionService webSocketSessionService;

    @Bean
    public WebFilter httpCookieFilter() {
        return new HttpCookieFilter(new SessionRepository(databaseClient));
    }

    @Bean
    public WebFilter webSocketUpgradeFilter() {
        return new WebSocketUpgradeFilter(webSocketSessionService);
    }

    @Bean
    public WebFilter corsFilter() {
        return new CorsFilter();
    }
}
