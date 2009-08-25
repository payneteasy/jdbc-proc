package com.googlecode.jdbcproc.daofactory.it.testdao.domain;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Employee only, without company
 */
public class EmployeeOnly implements Serializable{
    /** Id */
    @Column(name = "employee_id") @Id
    public long getId() { return theId ; }
    public void setId(long aId) { theId = aId ; }

    /** Firstname */
    @Column( name = "firstname" )
    public String getFirstname() { return theFirstname ; }
    public void setFirstname(String aFirstname) { theFirstname = aFirstname ; }

    /** Lastname */
    @Column( name = "lastname" )
    public String getLastname() { return theLastname ; }
    public void setLastname(String aLastname) { theLastname = aLastname ; }

    public String toString() {
        return "EmployeeOnly{" +
                "lastname='" + theLastname + '\'' +
                ", firstname='" + theFirstname + '\'' +
                ", id=" + theId +
                '}';
    }

    /** Lastname */
    private String theLastname ;
    /** Firstname */
    private String theFirstname ;
    /** Id */
    private long theId ;

}
