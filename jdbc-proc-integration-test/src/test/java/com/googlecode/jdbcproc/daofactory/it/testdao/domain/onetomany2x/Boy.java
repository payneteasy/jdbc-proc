package com.googlecode.jdbcproc.daofactory.it.testdao.domain.onetomany2x;

import javax.persistence.Column;

public class Boy {
    private String name;

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
