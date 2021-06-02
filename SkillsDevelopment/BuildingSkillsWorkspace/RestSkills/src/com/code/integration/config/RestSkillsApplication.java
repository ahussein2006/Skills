package com.code.integration.config;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.code.integration.services.CustomersService;

@ApplicationPath("/api")
public class RestSkillsApplication extends Application {
    private Set<Object> singletons = new HashSet<Object>();
    private Set<Class<?>> classes = new HashSet<Class<?>>();

    public RestSkillsApplication() {
	singletons.add(new CustomersService());
//	singletons.add(new SecurityFilter());
//	singletons.add(new LoggingFilter());
	singletons.add(new LoggingFilterInterceptor());
    }

    @Override
    public Set<Class<?>> getClasses() {
	return classes;
    }

    @Override
    public Set<Object> getSingletons() {
	return singletons;
    }
}
