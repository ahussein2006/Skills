package com.code.service.config;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.code.config.InjectionManager;
import com.code.service.providers.MissionsService;

@ApplicationPath("/api")
public class ServicesInitializer extends Application {
    private Set<Object> singletons = new HashSet<Object>();
    private Set<Class<?>> classes = new HashSet<Class<?>>();

    public ServicesInitializer() {
	singletons.add(new MissionsService());
	InjectionManager.wireServices(singletons);

	singletons.add(new ServicesLogger());
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