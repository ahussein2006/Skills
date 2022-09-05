package com.code.service.config;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

@Provider
@PreMatching
public class ServicesLogger implements ContainerRequestFilter, WriterInterceptor {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
	System.out.println("Request: " + requestContext.getUriInfo().getAbsolutePath());
	requestContext.setEntityStream(logInputStream(requestContext.getEntityStream()));
    }

    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
	OutputStream outputStream = context.getOutputStream();
	ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	context.setOutputStream(byteArrayOutputStream);
	context.proceed();
	System.out.println(byteArrayOutputStream.toString());
	byteArrayOutputStream.writeTo(outputStream);
	byteArrayOutputStream.close();
	context.setOutputStream(outputStream);
    }

    private ByteArrayInputStream logInputStream(InputStream inputStream) throws IOException {
	Scanner scanner = new Scanner(inputStream);
	StringBuilder stringBuilder = new StringBuilder();
	while (scanner.hasNext())
	    stringBuilder.append(scanner.nextLine());
	scanner.close();
	System.out.println(stringBuilder.toString());
	return new ByteArrayInputStream(stringBuilder.toString().getBytes());
    }

}