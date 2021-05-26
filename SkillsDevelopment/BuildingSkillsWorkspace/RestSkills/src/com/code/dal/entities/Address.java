package com.code.dal.entities;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "address")
public class Address {

    private String street;
    private String city;
    private String state;
    private String zip;
    private String country;
    private boolean active;

    public String getStreet() {
	return street;
    }

    public void setStreet(String street) {
	this.street = street;
    }

    public String getCity() {
	return city;
    }

    public void setCity(String city) {
	this.city = city;
    }

    public String getState() {
	return state;
    }

    public void setState(String state) {
	this.state = state;
    }

    @XmlElement(nillable = true)
    @JsonbProperty(nillable = true)
    public String getZip() {
	return zip;
    }

    public void setZip(String zip) {
	this.zip = zip;
    }

    public String getCountry() {
	return country;
    }

    public void setCountry(String country) {
	this.country = country;
    }

    @XmlTransient
    @JsonbTransient
    public boolean isActive() {
	return active;
    }

    public void setActive(boolean active) {
	this.active = active;
    }
}
