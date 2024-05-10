package com.sixback.omesschat.domain.session.repository;

import io.r2dbc.spi.Row;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.session.MapSession;
import org.springframework.session.ReactiveSessionRepository;
import org.springframework.session.Session;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.Instant;

@RequiredArgsConstructor
public class SessionRepository implements ReactiveSessionRepository<Session> {

    private final DatabaseClient databaseClient;

    @Override
    public Mono<Session> createSession() {
        throw new IllegalArgumentException();
    }

    @Override
    public Mono<Void> save(Session session) {
        throw new IllegalArgumentException();
    }

    @Override
    public Mono<Session> findById(String id) {
        return databaseClient.sql("SELECT * FROM SPRING_SESSION WHERE SESSION_ID = :id")
                .bind("id", id)
                .map((row, metaData) -> mapRowToSession(row))
                .one();
    }

    public Mono<Long> findMemberIdBySessionId(String sessionId) {
        return databaseClient.sql("SELECT SA.ATTRIBUTE_BYTES " +
                        "FROM SPRING_SESSION_ATTRIBUTES SA " +
                        "JOIN SPRING_SESSION S ON S.PRIMARY_ID = SA.SESSION_PRIMARY_ID " +
                        "WHERE SA.ATTRIBUTE_NAME='memberId' AND S.SESSION_ID = :id")
                .bind("id", sessionId)
                .map((row, metaData) -> {
                    byte[] bytes = row.get("ATTRIBUTE_BYTES", byte[].class);
                    return getMemberId(bytes);
                })
                .one()
                .onErrorResume(e -> {
                    e.printStackTrace();
                    return Mono.empty(); // 오류 발생 시 빈 Mono 반환
                });
    }

    private Long getMemberId(byte[] bytes) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bis)) {

            return (Long) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalArgumentException(e.getMessage(), e.getCause());
        }
    }

    private Session mapRowToSession(Row row) {
        MapSession mapSession = new MapSession();
        mapSession.setId(row.get("SESSION_ID", String.class));
        mapSession.setLastAccessedTime(Instant.ofEpochMilli(row.get("LAST_ACCESS_TIME", Long.class)));

        return mapSession;
    }

    @Override
    public Mono<Void> deleteById(String id) {
        throw new IllegalArgumentException();
    }
}
