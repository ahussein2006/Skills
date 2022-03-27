package com.code;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/test")
public class TestRs {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getCustomers() {
	return "Test OK";
    }
}
