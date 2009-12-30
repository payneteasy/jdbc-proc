package com.googlecode.jdbcproc.daofactory.it.testdao.domain.onetomany2x;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

/**
 * Certificate for 2x tests
 */
public class Certificate2x {
    /** Identificator */
    @Id
    @Column(name = "id")
    public long getId() { return theId; }
    public void setId(long aId) { theId = aId; }

    /** Name */
    @Column(name = "name")
    public String getName() { return theName; }
    public void setName(String aName) { theName = aName; }

    /** Employee */
    @ManyToOne
    @JoinColumn( name = "employee_id" )
    public Employee2x getEmployee() { return theEmployee; }
    public void setEmployee(Employee2x aEmployee) { theEmployee = aEmployee; }

    /** Employee */
    private Employee2x theEmployee;
    /** Name */
    private String theName;
    /** Identificator */
    private long theId;

}
