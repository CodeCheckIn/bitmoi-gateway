package com.bitmoi.gateway.filter;

import java.nio.charset.StandardCharsets;

import com.bitmoi.gateway.util.jwtUtil;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

// 사용자 정의 필터를 만들기 위해 Spring Cloud Gateway가 추상화해 놓은 AbstractGatewayFilterFactory를 상속받는다.

@Component
public class JwtAdminAuthVerifyFilter extends AbstractGatewayFilterFactory<JwtAdminAuthVerifyFilter.Config> {

    private final jwtUtil jwtUtils;

    public JwtAdminAuthVerifyFilter(jwtUtil jwtUtils) {
        super(Config.class);
        this.jwtUtils = jwtUtils;
    }

    // 토큰 검증을 할 로직을 apply 메서드에 추가하면 된다.
    @Override
    public GatewayFilter apply(Config config) {

        // apply 메서드 안에서 첫 번째 파라미터는 ServerWebExchange 형태고, 두 번째 파라미터가 GatewayFilterChain
        // 람다 함수이다.
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

    // private void addAuthorizationHeaders(ServerHttpRequest request, TokenUser
    // tokenUser) {
    // request.mutate()
    // .header("X-Authorization-Id", tokenUser.getId())
    // // .header("X-Authorization-Role", tokenUser.getRole())
    // .build();
    // }
}