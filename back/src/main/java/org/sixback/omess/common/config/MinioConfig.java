package org.sixback.omess.common.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {
    @Value("${minio.url}")
    private String URL;

    @Value("${minio.key.access}")
    private String ACCESS_KEY;

    @Value("${minio.key.secret}")
    private String SECRET_KEY;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(URL)
                .credentials(ACCESS_KEY, SECRET_KEY)
                .build();
    }
}
