package com.googlecode.jdbcproc.daofactory.impl.block.service;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity(name = "test_entry")
public class TEntry {
    private int a;
    private String b;

    @Column(name = "a")
    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    @Column(name = "b")
    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }
}
