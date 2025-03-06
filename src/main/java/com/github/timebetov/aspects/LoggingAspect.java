package com.github.timebetov.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* com.github.timebetov..*.*(..))")
    public void beforeLog(JoinPoint joinPoint) throws Throwable {

        String methodName = joinPoint.getSignature().getName();
        log.info("Method: {} execution started...", methodName);
    }

    @Around("execution(* com.github.timebetov..*.*(..))")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {

        Instant start = Instant.now();
        Object returnObj = joinPoint.proceed();
        Instant finish = Instant.now();

        long timeElapsed = Duration.between(start, finish).toMillis();
        String methodName = joinPoint.getSignature().getName();

        log.info("Method: {} executed in: {} ms.", methodName, timeElapsed);

        return returnObj;
    }

    @After("execution(* com.github.timebetov..*.*(..))")
    public void afterLog(JoinPoint joinPoint) throws Throwable {

        String methodName = joinPoint.getSignature().getName();
        log.info("Method: {} execution end...", methodName);
    }

    @AfterReturning(value = "execution(* com.github.timebetov..*.*(..))", returning = "result")
    public void logReturn(JoinPoint joinPoint, Object result) {

        String methodName = joinPoint.getSignature().getName();
        log.info("Method: {} execution return result: {}", methodName, result);
    }

    @AfterThrowing(value = "execution(* com.github.timebetov..*.*(..))", throwing = "ex")
    public void logException(JoinPoint joinPoint, Throwable ex) {

        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        log.error("Execution: {} in {} failed due to: {} ", methodName, className, ex.getMessage());
    }
}
