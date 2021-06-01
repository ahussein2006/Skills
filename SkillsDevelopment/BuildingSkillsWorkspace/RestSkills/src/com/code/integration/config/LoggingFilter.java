package com.code.integration.config;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.message.internal.ReaderWriter;

@Provider
@PreMatching
public class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
	System.out.println("Logging");
	requestContext.setEntityStream(logInputStream(requestContext.getEntityStream()));
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
	    throws IOException {
	System.out.println(responseContext.getEntity() != null ? responseContext.getEntity().toString() : "");
    }

    private ByteArrayInputStream logInputStream(InputStream inputStream) throws IOException {
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	ReaderWriter.writeTo(inputStream, out);
	byte[] requestByteArray = out.toByteArray();
	System.out.println(new String(requestByteArray));
	return new ByteArrayInputStream(requestByteArray);
    }
}