package com.googlecode.jdbcproc.daofactory.it.testdao.model;

import javax.persistence.Id;
import javax.persistence.Column;

public class ChalkBag {

    private long chalkBagId;
    private String name;
    private String color;
    private String materials;

    @Id
    @Column(name = "chalk_bag_id")
    public long getChalkBagId() {
        return chalkBagId;
    }

    public void setChalkBagId(long chalkBagId) {
        this.chalkBagId = chalkBagId;
    }

    @Column(name = "chalk_bag_name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "color")
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Column(name = "materials")
    public String getMaterials() {
        return materials;
    }

    public void setMaterials(String materials) {
        this.materials = materials;
    }
}