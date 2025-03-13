package com.hrworkflow.jobservice.aspects;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
@Aspect
public class ExceptionHandlingAspect {

    @Value("${app.topics.log-error}")
    private String logErrorTopic;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Around(value = "execution (* com.hrworkflow.jobservice..*.*(..))")
    public Object handleExceptions(ProceedingJoinPoint joinPoint) throws Throwable {

        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            String errorMsg = String.format(
                    "{\"error\": \"%s\", \"method\": \"%s\", \"class\": \"%s\"}",
                    e.getClass().getSimpleName(),
                    joinPoint.getSignature().toShortString(),
                    joinPoint.getTarget().getClass().getSimpleName()
            );
            kafkaTemplate.send(logErrorTopic, errorMsg);
            log.error("Unexpected error: {}", errorMsg);

            throw e;
        }
    }
}
