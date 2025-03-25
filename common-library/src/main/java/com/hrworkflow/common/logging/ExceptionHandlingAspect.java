package com.hrworkflow.common.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrworkflow.common.dto.ErrorDTO;
import com.hrworkflow.common.exceptions.CustomServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ExceptionHandlingAspect {

    private final ObjectMapper objectMapper;

    @Around(value = "execution (* com.hrworkflow..*.service..*.*(..))")
    public Object handleExceptions(ProceedingJoinPoint joinPoint) throws Throwable {

        try {
            return joinPoint.proceed();
        } catch (NullPointerException e) {
            log.error("NullPointerException in {}: {}", joinPoint.getSignature().toShortString(), e.getMessage());
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .error(e.getClass().getSimpleName())
                    .method(joinPoint.getSignature().toShortString())
                    .className(joinPoint.getTarget().getClass().getName())
                    .message(e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
            String jsonError = objectMapper.writeValueAsString(errorDTO);

            log.error("Unexpected error: {}", jsonError);
            throw new CustomServiceException("Unexpected null value");
        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .error(e.getClass().getSimpleName())
                    .method(joinPoint.getSignature().toShortString())
                    .className(joinPoint.getTarget().getClass().getName())
                    .message(e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();

            String jsonError = objectMapper.writeValueAsString(errorDTO);

            log.error("Unexpected error: {}", jsonError);
            throw e;
        }
    }
}
