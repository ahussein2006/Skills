package com.code.appconfig;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.code.integration.webservices.CustomersWS;

@ApplicationPath("/services")
public class RestSkillsApplication extends Application {
    private Set<Object> singletons = new HashSet<Object>();
    private Set<Class<?>> classes = new HashSet<Class<?>>();

    public RestSkillsApplication() {
	// singletons.add(new CustomersWS());
	classes.add(CustomersWS.class);
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
