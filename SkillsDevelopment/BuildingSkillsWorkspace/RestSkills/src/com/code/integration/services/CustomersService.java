package com.code.integration.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import com.code.Utils;
import com.code.dal.entities.Address;
import com.code.dal.entities.Customer;

@Path("/customers")
public class CustomersService {

    private Map<Integer, Customer> customerDB = new ConcurrentHashMap<Integer, Customer>();
    private AtomicInteger idCounter = new AtomicInteger();

    @POST
    @Consumes("application/xml")
    public Response makeCustomer(InputStream is) {
	Customer customer = readCustomer(is);

	customer.setId(idCounter.incrementAndGet());
	customerDB.put(customer.getId(), customer);
	System.out.println("Created customer " + customer.getId());
	return Response.created(URI.create("/customers/" + customer.getId())).build();
    }

    @GET
    @Path("{id}")
    @Produces("application/xml")
    public StreamingOutput getCustomer(@PathParam("id") int id) {
	final Customer customer = customerDB.get(id);
	if (customer == null) {
	    throw new WebApplicationException(Response.Status.NOT_FOUND);
	}
	return new StreamingOutput() {
	    public void write(OutputStream outputStream)
		    throws IOException, WebApplicationException {
		outputCustomer(outputStream, customer);
	    }
	};
    }

    @GET
    @Path("byId/{id}")
    @Produces({ "application/xml", "application/json" })
    public Customer getCustomerById(@PathParam("id") int id) {
	Customer customer = customerDB.get(id);
	if (customer == null) {
	    throw new WebApplicationException(Response.Status.NOT_FOUND);
	}
	return customer;
    }

    @PUT
    @Path("{id}")
    @Consumes("application/xml")
    public void updateCustomer(@PathParam("id") int id, InputStream is) {
	Customer update = readCustomer(is);

	Customer current = customerDB.get(id);
	if (current == null)
	    throw new WebApplicationException(Response.Status.NOT_FOUND);

	current.setFirstName(update.getFirstName());
	current.setLastName(update.getLastName());
	current.setAddress(update.getAddress());
    }

    protected void outputCustomer(OutputStream os, Customer cust) throws IOException {
	PrintStream writer = new PrintStream(os);

	writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
	writer.println("<customer id=\"" + cust.getId() + "\">");
	writer.println(" <first-name>" + cust.getFirstName() + "</first-name>");
	writer.println(" <last-name>" + cust.getLastName() + "</last-name>");
	writer.println(" <street>" + cust.getAddress().getStreet() + "</street>");
	writer.println(" <city>" + cust.getAddress().getCity() + "</city>");
	writer.println(" <state>" + cust.getAddress().getState() + "</state>");
	writer.println(" <zip>" + cust.getAddress().getZip() + "</zip>");
	writer.println(" <country>" + cust.getAddress().getCountry() + "</country>");
	writer.println("</customer>");
    }

    protected Customer readCustomer(InputStream is) {
	Customer customer = new Customer();
	Address address = new Address();

	String customerDataStr = Utils.readCustomer(is);
	String[] customerDataInfo = customerDataStr.split(",");
	int index = 0;
	if (customerDataInfo.length == 9) {
	    customer.setId(Integer.valueOf(customerDataInfo[index++]));
	}
	customer.setFirstName(customerDataInfo[index++]);
	customer.setLastName(customerDataInfo[index++]);

	address.setStreet(customerDataInfo[index++]);
	address.setCity(customerDataInfo[index++]);
	address.setState(customerDataInfo[index++]);
	address.setZip(customerDataInfo[index++]);
	address.setCountry(customerDataInfo[index++]);

	customer.setAddress(address);

	return customer;
    }

}
