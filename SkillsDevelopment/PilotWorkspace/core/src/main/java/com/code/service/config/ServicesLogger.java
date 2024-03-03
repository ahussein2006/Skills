package com.code.service.config;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

import com.code.enums.JsonAttributesEnum;
import com.code.enums.LogTypesEnum;
import com.code.security.SecurityManager;
import com.code.util.ContentUtil;
import com.code.util.IOStreamUtil;
import com.code.util.LoggingUtil;

@Provider
@PreMatching
public class ServicesLogger implements ContainerRequestFilter, WriterInterceptor {

    // TODO: Apply system configuration
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
	SecurityManager.setUserSecurityInfo("Ahmed", 1L);
	String message = IOStreamUtil.getInputStreamString(requestContext.getEntityStream());
	requestContext.setEntityStream(new ByteArrayInputStream(message.getBytes()));

	LoggingUtil.log(message, SecurityManager.getUserId("Ahmed"), requestContext.getUriInfo().getAbsolutePath().toString(), ContentUtil.getValueFromJsonString(message, JsonAttributesEnum.PROVIDER_REQUEST_ID.getValue()), LogTypesEnum.LOG_PROVIDER_REQUEST);
    }

    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
	OutputStream outputStream = context.getOutputStream();
	ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	context.setOutputStream(byteArrayOutputStream);
	context.proceed();
	String message = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
	byteArrayOutputStream.writeTo(outputStream);
	byteArrayOutputStream.close();
	context.setOutputStream(outputStream);

	LoggingUtil.log(message, null, null, ContentUtil.getValueFromJsonString(message, JsonAttributesEnum.PROVIDER_REQUEST_ID.getValue()), LogTypesEnum.LOG_PROVIDER_RESPONSE);
    }
}
