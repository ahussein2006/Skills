package com.code.integration.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.code.business.TestBusiness;
import com.code.dal.entities.um.audit.AuditLog;

@Path("/test")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TestService {

    @Autowired
    private TestBusiness testBusiness;

    @GET
    @Path("{id}")
    public AuditLog getAuditLogById(@PathParam("id") String contentId) {
	return testBusiness.getAuditLogById(contentId);
    }

    @POST
    public void getAuditLogById(AuditLog auditLog) {
	testBusiness.addAuditLog(auditLog);
    }
}
