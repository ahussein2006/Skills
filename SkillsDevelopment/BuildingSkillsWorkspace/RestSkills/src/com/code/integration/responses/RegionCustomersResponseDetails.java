package com.code.integration.responses;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.code.dal.entities.Customer;

@XmlRootElement(name = "responseDetails")
public class RegionCustomersResponseDetails {

    private List<Customer> regionCustomers;

    public RegionCustomersResponseDetails() {

    }

    public RegionCustomersResponseDetails(List<Customer> regionCustomers) {
	this.regionCustomers = regionCustomers;
    }

    @XmlElementWrapper(name = "regionCustomers")
    @XmlElement(name = "customer")
    public List<Customer> getRegionCustomers() {
	return regionCustomers;
    }

    public void setRegionCustomers(List<Customer> regionCustomers) {
	this.regionCustomers = regionCustomers;
    }

}
