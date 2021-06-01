package com.code.integration.config;

import java.io.IOException;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;

@Provider
@PreMatching
public class SecurityFilter implements ContainerRequestFilter {
    public void filter(ContainerRequestContext requestContext) throws IOException {
	System.out.println("Security");

	String securityHeader = requestContext.getHeaderString("Security-Token");

	if (securityHeader == null)
	    throw new NotAuthorizedException("Missing Security-Token");

	if (securityHeader.equals("HACKER"))
	    throw new NotAuthorizedException("Invalid Security-Token");

    }
}