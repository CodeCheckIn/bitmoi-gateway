package com.bitmoi.gateway.filter;

import java.nio.charset.StandardCharsets;
import com.bitmoi.gateway.util.jwtUtil;
import com.google.common.net.HttpHeaders;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtAuthentication
        extends AbstractGatewayFilterFactory<JwtAuthentication.Config> {

    private final jwtUtil jwtUtils;

    @Override
    public GatewayFilter apply(Config config) {

        return ((exchange, chain) -> {

            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
            // Request Header에 token이 존재하지 않을 경우
            if (!containsAuthorization(request)) {
                return onError(response, "헤더가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
            }

            // Request Header에서 token 추출

            String token = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (!jwtUtils.isValid(token)) {
                return onError(response, "맞지 않은 토큰입니다.", HttpStatus.BAD_REQUEST);
            }

            return chain.filter(exchange);
        });
    }

    private boolean containsAuthorization(ServerHttpRequest request) {
        return request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION);
    }

    private Mono<Void> onError(ServerHttpResponse response, String message, HttpStatus status) {
        response.setStatusCode(status);
        DataBuffer buffer = response.bufferFactory().wrap(message.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }

    // Config class에서는 Configuration 속성들을 넣어주면 되는데, Global Filter를 보자.
    public static class Config {

    }
}