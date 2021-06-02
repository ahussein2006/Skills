package com.code.integration.config;

import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

public class LoggingHandler implements SOAPHandler<SOAPMessageContext> {

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
//	Boolean isResponse = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
	try {
	    SOAPBody soapBody = context.getMessage().getSOAPPart().getEnvelope().getBody();
	    System.out.println(soapBody.toString());

	} catch (SOAPException e) {
	    e.printStackTrace();
	}
//	if (!isResponse) {
//	    System.out.println("Request");
//	    System.out.println(isResponse);
//	} else {
//	    System.out.println("Response");
//	    System.out.println(isResponse);
//	}
	return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
	return true;
    }

    @Override
    public void close(MessageContext context) {

    }

    @Override
    public Set<QName> getHeaders() {
	return null;
    }

}