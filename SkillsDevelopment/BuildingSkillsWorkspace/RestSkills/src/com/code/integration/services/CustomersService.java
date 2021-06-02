package com.code.integration.services;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
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

import com.code.dal.entities.Address;
import com.code.dal.entities.Customer;
import com.code.integration.responses.RegionCustomersResponse;

@WebService(targetNamespace = "http://integration.code.com/cutomers", portName = "CustomersServiceHttpPort")
@HandlerChain(file = "../config/handler-chain.xml")
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
    @Path("byId/{id}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @WebMethod(operationName = "getCustomerById")
    @WebResult(name = "customer")
    public Customer getCustomerById(@WebParam(name = "id") @PathParam("id") int id) {
	Customer c = new Customer();
	c.setId(12);
	c.setFirstName("Ahmed");
	c.setLastName("Moahmed");

	Address a = new Address();
	a.setCity("Alex");
	a.setCountry("EGY");
	a.setState("Alex");
	a.setStreet("Street 1");
	a.setZip("123456");

	c.setAddress(a);

	return c;
    }

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public List<Customer> getAllCustomers() {
	return customerDB.values().stream().collect(Collectors.toList());
    }

    @GET
    @Path("all")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @WebMethod(operationName = "getCustomers")
    @WebResult(name = "customer")
    public List<Customer> getCustomers() {
	List<Customer> customers = new ArrayList<Customer>();
	customers.add(getCustomerById(1));
	customers.add(getCustomerById(2));
	return customers;
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
