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

    /** Employee id */
    @Column(name = "employee_id")
    public Long getEmployeeId() { return theEmployeeId; }
    public void setEmployeeId(Long aEmployeeId) { theEmployeeId = aEmployeeId; }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Certificate2x that = (Certificate2x) o;

        if (theId != that.theId) return false;

        return true;
    }

    public int hashCode() {
        return (int) (theId ^ (theId >>> 32));
    }


    public String toString() {
        return "Certificate2x{" +
                "theEmployeeId=" + theEmployeeId +
                ", theName='" + theName + '\'' +
                ", theId=" + theId +
                '}';
    }

    /** Employee id */
    private Long theEmployeeId;
    /** Name */
    private String theName;
    /** Identificator */
    private long theId;

}
