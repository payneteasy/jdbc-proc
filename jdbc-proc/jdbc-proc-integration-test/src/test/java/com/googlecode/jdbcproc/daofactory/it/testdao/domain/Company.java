package com.googlecode.jdbcproc.daofactory.it.testdao.domain;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Company
 */
public class Company implements Serializable {
    /**
     * Idendificator
     */
    @Id
    @Column(name = "company_id")
    public long getId() {
        return theId;
    }

    public void setId(long aId) {
        theId = aId;
    }


    /**
     * Name
     */
    @Column(name = "name")
    public String getName() {
        return theName;
    }

    public void setName(String aName) {
        theName = aName;
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Company company = (Company) o;

        if (theId != company.theId) return false;
        return !(theName != null ? !theName.equals(company.theName) : company.theName != null);

    }


    public String toString() {
        return "Company{" +
                " id=" + theId +
                " , name='" + theName + '\'' +
                '}';
    }

    /**
     * Name
     */
    private String theName;
    /**
     * Idendificator
     */
    private long theId;

}
