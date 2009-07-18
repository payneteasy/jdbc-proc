package com.googlecode.jdbcproc.daofactory.it.testdao;

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
    @Column(name = "id")
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

    /**
     * Name
     */
    private String theName;
    /**
     * Idendificator
     */
    private long theId;

}
