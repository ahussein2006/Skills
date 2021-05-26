package com.code.integration.responses;

import java.util.List;

import javax.json.bind.annotation.JsonbProperty;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.code.integration.entities.Customer;

@XmlRootElement(name = "regionCustomersResponse")
public class RegionCustomersResponse {

    private Customer mainRegionCustomer;
    private RegionCustomersResponseDetails regionCustomersResponseDetails;

    public RegionCustomersResponse() {

    }

    public RegionCustomersResponse(Customer mainRegionCustomer, List<Customer> regionCustomers) {
	this.mainRegionCustomer = mainRegionCustomer;
	this.regionCustomersResponseDetails = new RegionCustomersResponseDetails(regionCustomers);
    }

    public Customer getMainRegionCustomer() {
	return mainRegionCustomer;
    }

    public void setMainRegionCustomer(Customer mainRegionCustomer) {
	this.mainRegionCustomer = mainRegionCustomer;
    }

    @XmlElement(name = "responseDetails")
    @JsonbProperty(value = "responseDetails")
    public RegionCustomersResponseDetails getRegionCustomersResponseDetails() {
	return regionCustomersResponseDetails;
    }

    @JsonbProperty(value = "responseDetails")
    public void setRegionCustomersResponseDetails(RegionCustomersResponseDetails regionCustomersResponseDetails) {
	this.regionCustomersResponseDetails = regionCustomersResponseDetails;
    }

}
