package com.hrworkflow.usersservice.aspects;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class LoggingAspect {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${app.topics.log-info}")
    private String infoLogTopic;

    @Around("execution(* com.hrworkflow.usersservice..*.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        Instant startTime = Instant.now();
        Object returnJob = joinPoint.proceed();
        Instant endTime = Instant.now();

        long duration = Duration.between(startTime, endTime).toMillis();

        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();

        String logMessage = String.format("{\"class\": \"%s\", \"method\": \"%s\", \"executionTime\": %d}",
                className, methodName, duration);
        kafkaTemplate.send(infoLogTopic, logMessage);
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
        String username = request.getRemoteUser();

        String logMessage = String.format(
                "{\"httpMethod\": \"%s\", \"ip\": \"%s\", \"url\": \"%s\", \"user\": \"%s\",\"status\": \"%s\"}",
                httpMethod, ip, uri, username, response != null ? response.getStatus() : 0
        );

        kafkaTemplate.send(infoLogTopic, logMessage);
        log.info(logMessage);

        return returnObj;
    }

    @Pointcut("execution(* org.springframework.kafka.core.KafkaTemplate.send(..))")
    public void kafkaSendMethods() {}

    @Around("kafkaSendMethods()")
    public Object logKafkaMsgs(ProceedingJoinPoint joinPoint) throws Throwable {

        Object[] args = joinPoint.getArgs();

        if (args.length >= 2 && args[1] instanceof String message) {

            log.info(message);
        }

        return joinPoint.proceed();
    }

}
