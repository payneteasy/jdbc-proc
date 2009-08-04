package com.googlecode.jdbcproc.daofactory.it.testdao.domain;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Employe
 */
public class Employee implements Serializable {
    /** Id */
    @Id
    @Column(name="employee_id")
    public long getId() { return theId ; }
    public void setId(long aId) { theId = aId ; }

    /** Firstname */
    @Column(name="firstname")
    public String getFirstname() { return theFirstname ; }
    public void setFirstname(String aFirstname) { theFirstname = aFirstname ; }

    /** Lastname */
    @Column(name="lastname")
    public String getLastname() { return theLastname ; }
    public void setLastname(String aLastname) { theLastname = aLastname ; }

    /** Company */
    @ManyToOne
    public Company getCompany() { return theCompany ; }
    public void setCompany(Company aCompany) { theCompany = aCompany ; }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Employee employee = (Employee) o;

        if (theId != employee.theId) return false;
        if (theCompany != null ? !theCompany.equals(employee.theCompany) : employee.theCompany != null) return false;
        if (theFirstname != null ? !theFirstname.equals(employee.theFirstname) : employee.theFirstname != null)
            return false;
        return !(theLastname != null ? !theLastname.equals(employee.theLastname) : employee.theLastname != null);

    }


    public String toString() {
        return "Employee{" +
                " id=" + theId +
                ", company=" + theCompany +
                ", lastname='" + theLastname + '\'' +
                ", firstname='" + theFirstname + '\'' +
                '}';
    }

    /** Company */
    private Company theCompany ;
    /** Lastname */
    private String theLastname ;
    /** Firstname */
    private String theFirstname ;
    /** Id */
    private long theId ;

}
