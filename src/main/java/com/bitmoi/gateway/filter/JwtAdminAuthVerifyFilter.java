package com.bitmoi.gateway.filter;

import java.util.Objects;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

// 사용자 정의 필터를 만들기 위해 Spring Cloud Gateway가 추상화해 놓은 AbstractGatewayFilterFactory를 상속받는다.

@Component
public class JwtAdminAuthVerifyFilter extends AbstractGatewayFilterFactory<JwtAdminAuthVerifyFilter.Config> {

    // private final JwtProvider jwtProvider;

    public JwtAdminAuthVerifyFilter() {
        super(Config.class);
        // this.jwtProvider = jwtProvider;
    }

    // 토큰 검증을 할 로직을 apply 메서드에 추가하면 된다.
    @Override
    public GatewayFilter apply(Config config) {

        // apply 메서드 안에서 첫 번째 파라미터는 ServerWebExchange 형태고, 두 번째 파라미터가 GatewayFilterChain
        // 람다 함수이다.
        return ((exchange, chain) -> {

            ServerHttpRequest request = exchange.getRequest(); // Request로 받아오면 Pre Filter가 적용된다. (주의 :
                                                               // reactive.ServerHttpRequest 여야 한다.)

            // Request Header에 token이 존재하지 않을 경우
            // if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            // throw new
            // UnauthorizedException(ExceptionMessage.AuthVerifyAccessDenied.getMessage());
            // }

            // Request Header에서 token 추출
            String token = request.getHeaders().get("token").get(0);
            System.out.println("tokentokentokentokentokentokentoken" + token);
            return chain.filter(exchange);
            // 토큰 검증 및 ADMIN 권한 확인
            // if (true) {
            // return chain.filter(exchange);
            // } else {
            // throw new
            // UnauthorizedException(ExceptionMessage.AuthVerifyAccessDenied.getMessage());
            // }
        });
    }

    // Config class에서는 Configuration 속성들을 넣어주면 되는데, Global Filter를 보자.
    public static class Config {

    }

    private void addAuthorizationHeaders(ServerHttpRequest request, TokenUser tokenUser) {
        request.mutate()
                .header("X-Authorization-Id", tokenUser.getId())
                // .header("X-Authorization-Role", tokenUser.getRole())
                .build();
    }
}