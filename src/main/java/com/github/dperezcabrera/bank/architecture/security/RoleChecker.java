package com.github.dperezcabrera.bank.architecture.security;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

@Component("roleChecker")
@AllArgsConstructor
public class RoleChecker {

    private final AuditorAware<String> autidorAware;

    public boolean isAdmin() {
        return autidorAware.getCurrentAuditor().map("admin"::equalsIgnoreCase).orElse(Boolean.FALSE);
    }
}
