package com.code.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.code.dal.RepositoryManager;
import com.code.dal.entities.employees.EmployeeData;
import com.code.enums.QueryConfigConstants;
import com.code.exceptions.BusinessException;
import com.code.exceptions.RepositoryException;
import com.code.util.BasicUtil;
import com.code.util.ExceptionUtil;

@Service
public class EmployeesBusiness {

    @Autowired
    private RepositoryManager repositoryManager;

    // ----------------------------------- Employees Inquiries ---------------------------------
    public EmployeeData getEmployeeDataById(long employeeId) throws BusinessException {
	try {
	    return BasicUtil.getFirstItem(repositoryManager.getEntities(EmployeeData.class, QueryConfigConstants.HCM_EmployeeData_GetEmployeeDataById, QueryConfigConstants.HCM_EmployeeData_GetEmployeeDataById_Params, employeeId));
	} catch (RepositoryException e) {
	    throw ExceptionUtil.handleException(e, null);
	}
    }
}