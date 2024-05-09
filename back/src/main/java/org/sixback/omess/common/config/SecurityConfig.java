package org.sixback.omess.common.config;

import lombok.RequiredArgsConstructor;
import org.sixback.omess.common.security.CustomAccessDeniedHandler;
import org.sixback.omess.common.security.CustomAuthenticationEntryPoint;
import org.sixback.omess.common.security.ProjectPermissionFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.session.jdbc.JdbcIndexedSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

import static org.springframework.security.config.http.SessionCreationPolicy.NEVER;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final ProjectPermissionFilter projectPermissionFilter;
    private final JdbcIndexedSessionRepository jdbcIndexedSessionRepository;

    @Bean
    public SessionRegistry springSessionBackendSessionRegistry() {
        return new SpringSessionBackedSessionRegistry<>(jdbcIndexedSessionRepository);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .securityMatcher("/api/**")
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers("/api/v1/members/check-email**", "/api/v1/members/check-nickname**",
                                    "/api/v1/members/signup", "/api/v1/members/signin",
                                    "/api/v1/http-metadata/methods", "/api/v1/http-metadata/headers", "/api/v1/kanbanboards/**", "/api/v1/modules/**"
                            ).permitAll();
                    authorizationManagerRequestMatcherRegistry.anyRequest().authenticated();
                })
                .csrf(AbstractHttpConfigurer::disable)
                .headers(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .rememberMe(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .addFilterAfter(projectPermissionFilter, AuthorizationFilter.class)
                .sessionManagement((configurer -> {
                    configurer.sessionCreationPolicy((NEVER));
                    configurer.sessionFixation().migrateSession();
                }))
                .requestCache(configurer -> configurer.requestCache(new NullRequestCache()))
                .exceptionHandling(exceptionHandlingConfigurer -> {
                    exceptionHandlingConfigurer.authenticationEntryPoint(authenticationEntryPoint);
                    exceptionHandlingConfigurer.accessDeniedHandler(accessDeniedHandler);
                })
                .build()
                ;
    }
}
