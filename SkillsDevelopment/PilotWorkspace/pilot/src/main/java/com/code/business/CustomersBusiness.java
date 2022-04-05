package com.code.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.code.Test;

@Service
public class CustomersBusiness {

    @Autowired
    Test test;

    public String getCustomers() {
	return test.getMessage();
    }
}
