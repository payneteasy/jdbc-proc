package com.googlecode.jdbcproc.daofactory.it.testdao.dao;

import com.googlecode.jdbcproc.daofactory.annotation.AStoredProcedure;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.Employee;

/**
 * Data access for employee
 */
public interface IEmployeeDao {

    /**
     * Creates employee
     *
     * @param aEmployee new employe
     */
    @AStoredProcedure(name = "create_employee")
    void createEmployee(Employee aEmployee);

    /**
     * Gets employee by id
     *
     * @param id employee id
     * @return employee
     */
    @AStoredProcedure(name = "get_employee_by_id")
    Employee getEmployeeById(long id);
}
