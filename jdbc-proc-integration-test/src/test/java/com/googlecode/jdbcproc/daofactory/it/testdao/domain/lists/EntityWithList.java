package com.googlecode.jdbcproc.daofactory.it.testdao.domain.lists;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * @author rpuch
 */
public class EntityWithList {
    private Long id;
    private String name;

    @Column(name = "id")
    @Id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
