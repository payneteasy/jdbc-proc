package com.googlecode.jdbcproc.daofactory.it.testdao.domain.lists;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author rpuch
 */
@Entity(name = "list_elements")
public class ListElement {
    private String name;
    private String value;

    public ListElement() {
    }

    public ListElement(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
