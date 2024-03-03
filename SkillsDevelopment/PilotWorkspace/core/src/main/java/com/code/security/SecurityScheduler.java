package com.code.security;

import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SecurityScheduler {

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void invalidateUsersSecurityInfo() {
	SecurityManager.invalidateUsersSecurityInfo();
    }
}
