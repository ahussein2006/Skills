package com.code.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.code.Test;
import com.code.dal.entities.QueryNames;

@Service
public class CustomersBusiness {

    @Autowired
    Test test;

    public String getCustomers() {
	return QueryNames.HCM_CUSTOMER_GET_CUSTOMERS;
    }
}
