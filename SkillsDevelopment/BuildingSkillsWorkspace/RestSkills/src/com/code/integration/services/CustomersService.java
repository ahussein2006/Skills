package com.code.integration.services;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.code.dal.entities.Customer;
import com.code.integration.responses.RegionCustomersResponse;

@Path("/customers")
public class CustomersService {

    private Map<Integer, Customer> customerDB = new ConcurrentHashMap<Integer, Customer>();
    private AtomicInteger idCounter = new AtomicInteger();

    @POST
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Response makeCustomer(Customer customer) {
	if (customer.getId() != 0)
	    throw new WebApplicationException(Response.Status.BAD_REQUEST);

	customer.setId(idCounter.incrementAndGet());
	customerDB.put(customer.getId(), customer);
	System.out.println("Created customer " + customer.getId());
	return Response.created(URI.create("/customers/" + customer.getId())).build();
    }

    @GET
    @Path("{id}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Customer getCustomer(@PathParam("id") int id) {
	Customer customer = customerDB.get(id);
	if (customer == null)
	    throw new WebApplicationException(Response.Status.NOT_FOUND);

	return customer;
    }

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public List<Customer> getAllCustomers() {
	return customerDB.values().stream().collect(Collectors.toList());
    }

    @GET
    @Path("region")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public RegionCustomersResponse getRegionCustomers() {
	RegionCustomersResponse response = new RegionCustomersResponse(customerDB.get(1), getAllCustomers());
	return response;
    }

    @PUT
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public void updateCustomer(Customer customer) {
	Customer current = customerDB.get(customer.getId());
	if (current == null)
	    throw new WebApplicationException(Response.Status.NOT_FOUND);

	current.setFirstName(customer.getFirstName());
	current.setLastName(customer.getLastName());
	current.setAddress(customer.getAddress());
    }

    @DELETE
    @Path("{id}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public void deleteCustomer(@PathParam("id") int id) {
	if (customerDB.get(id) == null)
	    throw new WebApplicationException(Response.Status.NOT_FOUND);

	customerDB.remove(id);
    }

}
