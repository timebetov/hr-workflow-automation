package com.hrworkflow.apigateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    private final PathMatcher pathMatcher = new AntPathMatcher();

    public static final List<String> whiteList = List.of(
            "/identity/api/auth/**",
            "/actuator/**",
            "/eureka"
    );

    public Predicate<ServerHttpRequest> isSecured = request ->
                    whiteList.stream().noneMatch(uri -> pathMatcher.match(uri, request.getURI().getPath()));
}
