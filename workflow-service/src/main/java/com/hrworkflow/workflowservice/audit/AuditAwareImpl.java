package com.hrworkflow.workflowservice.audit;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@Component("auditAwareImpl")
public class AuditAwareImpl implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {

        RequestAttributes attr = RequestContextHolder.getRequestAttributes();

        if (attr instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) attr).getRequest();

            String userIdStr = request.getHeader("X-User-Id");

            if (userIdStr != null && !userIdStr.isEmpty()) {

                try {
                    return Optional.of(Long.parseLong(userIdStr));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid X-User-Id header");
                }
            }
        }

        throw new SecurityException("User ID is required");
    }
}
