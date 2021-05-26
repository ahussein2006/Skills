package com.code.integration.clients;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.code.integration.entities.Customer;
import com.code.integration.responses.RegionCustomersResponse;

public class CustomersClient extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	int id = Integer.parseInt(req.getParameter("id"));
	Client client = ClientBuilder.newClient();
	WebTarget idTarget = client.target("http://localhost:7001/RestSkills/services/customers/" + id);
	WebTarget baseTarget = client.target("http://localhost:7001/RestSkills/services/customers");
	WebTarget regionTarget = client.target("http://localhost:7001/RestSkills/services/customers/region");

	Customer jsonCustomer = idTarget.request().accept(MediaType.APPLICATION_JSON).get(Customer.class);
	System.out.println(jsonCustomer);
	jsonCustomer.setId(0);
	jsonCustomer.getAddress().setZip(null);
	jsonCustomer.getAddress().setCity("Alex JSON");
	jsonCustomer.getAddress().setAddressFlag("Any JSON");
	Response jsonResponse = baseTarget.request().post(Entity.json(jsonCustomer));
	jsonResponse.close();

	Customer xmlCustomer = idTarget.request().accept(MediaType.APPLICATION_XML).get(Customer.class);
	System.out.println(xmlCustomer);
	xmlCustomer.setId(0);
	xmlCustomer.getAddress().setZip(null);
	xmlCustomer.getAddress().setCity("Alex XML");
	xmlCustomer.getAddress().setAddressFlag("Any XML");
	Response xmlResponse = baseTarget.request().post(Entity.xml(xmlCustomer));
	xmlResponse.close();

	List<Customer> jsonCustmoers = baseTarget.request().accept(MediaType.APPLICATION_JSON)
		.get(new GenericType<List<Customer>>() {
		});
	printCustomers(jsonCustmoers);

	List<Customer> xmlCustmoers = baseTarget.request().accept(MediaType.APPLICATION_XML)
		.get(new GenericType<List<Customer>>() {
		});
	printCustomers(xmlCustmoers);

	RegionCustomersResponse jsonRegionCustomers = regionTarget.request().accept(MediaType.APPLICATION_JSON).get(RegionCustomersResponse.class);
	printRegionCustomers(jsonRegionCustomers);

	RegionCustomersResponse xmlRegionCustomers = regionTarget.request().accept(MediaType.APPLICATION_XML).get(RegionCustomersResponse.class);
	printRegionCustomers(xmlRegionCustomers);

	client.close();

	super.doGet(req, resp);
    }

    private static void printCustomers(List<Customer> customers) {
	for (Customer c : customers) {
	    System.out.println(c);
	}
    }

    private static void printRegionCustomers(RegionCustomersResponse regionCustomers) {
	System.out.println("--- Main Region Customer ---");
	System.out.println(regionCustomers.getMainRegionCustomer());
	System.out.println("--- Region Customers ---");
	for (Customer c : regionCustomers.getRegionCustomersResponseDetails().getRegionCustomers()) {
	    System.out.println(c);
	}
    }

}
