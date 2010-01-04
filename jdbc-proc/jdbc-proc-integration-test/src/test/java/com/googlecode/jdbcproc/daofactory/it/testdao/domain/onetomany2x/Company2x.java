package com.googlecode.jdbcproc.daofactory.it.testdao.domain.onetomany2x;

import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;
import java.util.List;

/**
 * Company for OneToMany2x test
 */
public class Company2x {

    /** Idendificator */
    @Id
    @Column(name = "id")
    public long getId() { return theId; }
    public void setId(long aId) { theId = aId; }

    /** Name */
    @Column(name = "name")
    public String getName() { return theName; }
    public void setName(String aName) { theName = aName; }

    /** list of Employees */
    @OneToMany
    @JoinColumn(table = "employee")
    public List<Employee2x> getEmployees() { return theEmployees; }
    public void setEmployees(List<Employee2x> aEmployees) { theEmployees = aEmployees; }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Company2x company2x = (Company2x) o;

        if (theId != company2x.theId) return false;

        return true;
    }

    public int hashCode() {
        return (int) (theId ^ (theId >>> 32));
    }


    public String toString() {
        return "Company2x{" +
                "theEmployees=" + theEmployees +
                ", theName='" + theName + '\'' +
                ", theId=" + theId +
                '}';
    }

    /** list of Employees */
    private List<Employee2x> theEmployees;
    /** Name */
    private String theName;
    /** Idendificator */
    private long theId;

}
