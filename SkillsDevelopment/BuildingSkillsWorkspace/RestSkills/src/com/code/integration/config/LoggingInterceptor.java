package com.code.integration.config;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

@Provider
public class LoggingInterceptor implements ReaderInterceptor, WriterInterceptor {

    @Override
    public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {
	context.setInputStream(logInputStream(context.getInputStream()));
	return context.proceed();
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
