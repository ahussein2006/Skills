package com.code.integration.config;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.code.config.InjectionManager;
import com.code.integration.services.TestService;

@ApplicationPath("/api")
public class ServicesInitializer extends Application {
    private Set<Object> singletons = new HashSet<Object>();
    private Set<Class<?>> classes = new HashSet<Class<?>>();

    public ServicesInitializer() {
	singletons.add(new TestService());
	InjectionManager.wireServices(singletons);
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