package com.googlecode.jdbcproc.daofactory.it.testdao.domain.lists;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * @author rpuch
 */
public class EntityWithList {
    private Long id;
    private String name;
    private String prop3;
    private String prop4;
    private String prop5;

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

    @Column(name = "prop3")
    public String getProp3() {
        return prop3;
    }

    public void setProp3(String prop3) {
        this.prop3 = prop3;
    }

    @Column(name = "prop4")
    public String getProp4() {
        return prop4;
    }

    public void setProp4(String prop4) {
        this.prop4 = prop4;
    }

    @Column(name = "prop5")
    public String getProp5() {
        return prop5;
    }

    public void setProp5(String prop5) {
        this.prop5 = prop5;
    }
}
