package com.hrworkflow.common.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;


@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LoggingAspect {

    @Around("execution(* com.hrworkflow..*.service..*.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        Instant startTime = Instant.now();
        Object returnJob = joinPoint.proceed();
        Instant endTime = Instant.now();

        long duration = Duration.between(startTime, endTime).toMillis();

        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();

        String logMessage = String.format("{\"class\": \"%s\", \"method\": \"%s\", \"executionTime\": %d}",
                className, methodName, duration);
        log.info(logMessage);
        return returnJob;
    }

    @Around("within(@org.springframework.web.bind.annotation.RestController *)")
    public Object logControllerCalls(ProceedingJoinPoint joinPoint) throws Throwable {

        Object returnObj = joinPoint.proceed();

        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse();

        String ip = request.getRemoteAddr();
        String uri = request.getRequestURI();
        String httpMethod = request.getMethod();
        String username = request.getRemoteUser() != null ? request.getRemoteUser() : "anonymous";
        int status = (response != null) ? response.getStatus() : 0;

        String logMessage = String.format(
                "{\"httpMethod\": \"%s\", \"ip\": \"%s\", \"url\": \"%s\", \"user\": \"%s\",\"status\": \"%s\"}",
                httpMethod, ip, uri, username, status
        );

        log.info(logMessage);

        return returnObj;
    }

    @Pointcut("execution(* org.springframework.kafka.core.KafkaTemplate.send(..))")
    public void kafkaSendMethods() {}

    @Around("kafkaSendMethods()")
    public Object logKafkaMsgs(ProceedingJoinPoint joinPoint) throws Throwable {

        Object[] args = joinPoint.getArgs();

        if (args.length >= 2 && args[0] instanceof String && args[1] instanceof String) {
            String topic = (String) args[0];
            String message = (String) args[1];
            log.info("Kafka Message Sent -> Topic: {}, Message: {}", topic, message);
        }

        return joinPoint.proceed();
    }

}
