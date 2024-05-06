package org.sixback.omess.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry){
        registry.enableSimpleBroker("/sub"); // sub가 prefix로 붙은 destination의 클라이언트에게 전송 가능하도록 Broker 등록
        registry.setApplicationDestinationPrefixes("/pub");  // pub가 prefix로 붙은 메시지들은 @MessageMapping이 붙은 method로 바운드
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")   //SockJS 연결 할 엔드 포인트
                .setAllowedOrigins("*") // cors 오류 방지 // FixMe 허용할 origin 등록
                .withSockJS(); //버전 낮은 브라우저에서도 적용 가능
    }

}
