package com.code.integration.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.code.business.CustomersBusiness;

@Path("/customers")
public class CustomersService {

    @Autowired
    private CustomersBusiness customersBusiness;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getCustomers() {
	return customersBusiness.getCustomers();
    }
}
