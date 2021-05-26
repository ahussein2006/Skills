package com.code.dal.entities;

import javax.json.bind.annotation.JsonbProperty;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "customer")
public class Customer {

    private int id;
    private String firstName;
    private String lastName;
    private Address address;

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public String getFirstName() {
	return firstName;
    }

    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    public String getLastName() {
	return lastName;
    }

    public void setLastName(String lastName) {
	this.lastName = lastName;
    }

    @XmlElement(name = "customerAddress")
    @JsonbProperty(value = "customerAddress")
    public Address getAddress() {
	return address;
    }

    @JsonbProperty(value = "customerAddress")
    public void setAddress(Address address) {
	this.address = address;
    }
}
