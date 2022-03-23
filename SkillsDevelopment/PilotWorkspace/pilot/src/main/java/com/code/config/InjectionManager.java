package com.code.config;

import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

@Configuration
public class InjectionManager implements ServletContextInitializer {

    private static WebApplicationContext webApplicationContext;

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
	webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
    }

    public static void wireServices(Set<Object> services) {
	services.stream().forEach(service -> webApplicationContext.getAutowireCapableBeanFactory().autowireBean(service));
    }
}