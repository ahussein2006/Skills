package com.code.integration.webservices;

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
import com.code.dal.entities.Customer;

@Path("/customers")
public class CustomersWS {

    private Map<Integer, Customer> customerDB = new ConcurrentHashMap<Integer, Customer>();
    private AtomicInteger idCounter = new AtomicInteger();

    @POST
    @Consumes("application/xml")
    public Response createCustomer(InputStream is) {
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
	current.setStreet(update.getStreet());
	current.setState(update.getState());
	current.setZip(update.getZip());
	current.setCountry(update.getCountry());
    }

    protected void outputCustomer(OutputStream os, Customer cust) throws IOException {
	PrintStream writer = new PrintStream(os);

	writer.println("<customer id=\"" + cust.getId() + "\">");
	writer.println(" <first-name>" + cust.getFirstName() + "</first-name>");
	writer.println(" <last-name>" + cust.getLastName() + "</last-name>");
	writer.println(" <street>" + cust.getStreet() + "</street>");
	writer.println(" <city>" + cust.getCity() + "</city>");
	writer.println(" <state>" + cust.getState() + "</state>");
	writer.println(" <zip>" + cust.getZip() + "</zip>");
	writer.println(" <country>" + cust.getCountry() + "</country>");
	writer.println("</customer>");
    }

    protected Customer readCustomer(InputStream is) {
	Customer customer = new Customer();
	String customerStr = Utils.readCustomer(is);
	String[] customerInfo = customerStr.split(",");
	int index = 0;
	if (customerInfo.length == 9) {
	    customer.setId(Integer.valueOf(customerInfo[index++]));
	}
	customer.setFirstName(customerInfo[index++]);
	customer.setLastName(customerInfo[index++]);
	customer.setStreet(customerInfo[index++]);
	customer.setCity(customerInfo[index++]);
	customer.setState(customerInfo[index++]);
	customer.setZip(customerInfo[index++]);
	customer.setCountry(customerInfo[index++]);

	return customer;
    }

}
