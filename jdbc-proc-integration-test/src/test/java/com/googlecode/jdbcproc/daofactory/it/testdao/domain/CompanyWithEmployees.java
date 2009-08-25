package com.googlecode.jdbcproc.daofactory.it.testdao.domain;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.List;

/**
 * Company with employees
 */
public class CompanyWithEmployees implements Serializable {
    /** Id */
    @Column( name = "company_id" )
    public long getId() { return theId ; }
    public void setId(long aId) { theId = aId ; }

    /** Company name */
    @Column( name = "name" )
    public String getName() { return theName ; }
    public void setName(String aName) { theName = aName ; }

    /** Employees */
    @OneToMany
    @JoinColumn( table = "employee" )
    public List<EmployeeOnly> getEmployees() { return theEmployees ; }
    public void setEmployees(List<EmployeeOnly> aEmployees) { theEmployees = aEmployees ; }


    public String toString() {
        return "CompanyWithEmployees{" +
                "employees=" + theEmployees +
                ", name='" + theName + '\'' +
                ", id=" + theId +
                '}';
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompanyWithEmployees that = (CompanyWithEmployees) o;

        return theId == that.theId;

    }

    public int hashCode() {
        return (int) (theId ^ (theId >>> 32));
    }

    /** Employees */
    private List<EmployeeOnly> theEmployees ;
    /** Company name */
    private String theName ;
    /** Id */
    private long theId ;

}
