package com.hrworkflow.apigateway.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrworkflow.apigateway.dto.Token;
import com.hrworkflow.apigateway.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class JwtAuthFilter extends AbstractGatewayFilterFactory<JwtAuthFilter.Config> {

    private final RedisTemplate<String, Token> redisTemplate;
    private final RouteValidator validator;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Autowired
    public JwtAuthFilter(RouteValidator routeValidator, RedisTemplate<String, Token> redisTemplate, JwtUtil jwtUtil, ObjectMapper objectMapper) {
        super(Config.class);
        this.validator = routeValidator;
        this.redisTemplate = redisTemplate;
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (((exchange, chain) -> {

            ServerHttpRequest request = exchange.getRequest();

            if (validator.isSecured.test(request)) {
                // Header contains Token
                if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    String message = "Missing Authorization header";
                    log.warn(message);
                    return unauthorizedResponse(exchange, message);
                }

                String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    String message = "Invalid Authorization header format";
                    log.warn(message);
                    return unauthorizedResponse(exchange, message);
                }
                String token = authHeader.substring(7).trim();

                Token cachedToken = redisTemplate.opsForValue().get(token);
                if (cachedToken == null) {
                    String message = "Token not found";
                    log.warn(message + " in Redis");
                    return unauthorizedResponse(exchange, message);
                }

                if (cachedToken.getExpiresAt().before(new Date())) {
                    String message = "Token has expired";
                    log.warn(message);
                    return unauthorizedResponse(exchange, message);
                }

                if (!jwtUtil.validateToken(token, cachedToken.getUsername())) {
                    String message = "Token is not valid";
                    log.warn(message);
                    return unauthorizedResponse(exchange, message);
                }

                log.info("Token validated successfully");

                // Extracting the userId and username from Token not JWT
                String userId = String.valueOf(cachedToken.getUserId());
                String username = cachedToken.getUsername();
                String userRole = cachedToken.getUserRole();

                ServerHttpRequest modifiedRequest = request.mutate()
                        .header("X-User-Id", userId)
                        .header("X-Username", username)
                        .header("X-User-Role", userRole)
                        .build();

                return chain.filter(exchange.mutate().request(modifiedRequest).build());
            }

            return chain.filter(exchange);
        }));
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String message) {

        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        try {
            String json = objectMapper.writeValueAsString(Map.of("message", message));
            byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
            return exchange.getResponse().writeWith(Mono.just(buffer));
        } catch (Exception e) {
            return exchange.getResponse().setComplete();
        }
    }

    public static class Config {}
}
