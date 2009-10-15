package com.googlecode.jdbcproc.daofactory.it.testdao.model;

import javax.persistence.Entity;
import javax.persistence.Column;

@Entity(name = "harnesses")
public class Harness {

    private final String name;
    private final double weight;
    private final String color;
    private final String size;

    public static class Builder {
        private final String name;
        private double weight;
        private String color;
        private String size;

        public Builder(String name) {
            this.name = name;
        }

        public Builder weight(double val) {
            weight = val;
            return this;
        }

        public Builder color(String val) {
            color = val;
            return this;
        }

        public Builder size(String val) {
            size = val;
            return this;
        }

        public Harness build() {
            return new Harness(this);
        }
    }

    private Harness(Builder builder) {
        this.name = builder.name;
        this.weight = builder.weight;
        this.color = builder.color;
        this.size = builder.size;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    @Column(name = "weight")
    public double getWeight() {
        return weight;
    }

    @Column(name = "color")
    public String getColor() {
        return color;
    }

    @Column(name = "size")
    public String getSize() {
        return size;
    }
}