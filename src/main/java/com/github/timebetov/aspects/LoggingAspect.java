package com.github.timebetov.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* com.github.timebetov.repository.*.*(..))")
    public void beforeLog(JoinPoint joinPoint) throws Throwable {

        String methodName = joinPoint.getSignature().getName();
        log.info("Method execution started: " + methodName);
    }

    @Around("execution(* com.github.timebetov.repository.*.*(..))")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {

        Instant start = Instant.now();
        Object returnObj = joinPoint.proceed();
        Instant finish = Instant.now();

        long timeElapsed = Duration.between(start, finish).toMillis();
        String methodName = joinPoint.getSignature().getName();

        log.info("Method: {} executed in: {} ms.", methodName, timeElapsed);

        return returnObj;
    }

    @After("execution(* com.github.timebetov.repository.*.*(..))")
    public void afterLog(JoinPoint joinPoint) throws Throwable {

        String methodName = joinPoint.getSignature().getName();
        log.info("Method execution end: " + methodName);
    }

    @AfterReturning(value = "execution(* com.github.timebetov.repository.*.*(..))", returning = "result")
    public void logReturn(JoinPoint joinPoint, Object result) {

        String methodName = joinPoint.getSignature().getName();
        log.info("Method execution return: " + methodName + " with result: " + result);
    }

    @AfterThrowing(value = "execution(* com.github.timebetov.repository.*.*(..))", throwing = "ex")
    public void logException(JoinPoint joinPoint, Throwable ex) {

        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        log.error("Execution: "+ methodName + " in: " + className + " failed with exception: " + ex.getMessage());
    }
}
