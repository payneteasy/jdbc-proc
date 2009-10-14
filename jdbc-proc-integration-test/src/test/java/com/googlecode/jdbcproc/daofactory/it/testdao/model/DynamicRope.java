package com.googlecode.jdbcproc.daofactory.it.testdao.model;

import javax.persistence.Entity;
import javax.persistence.Column;

@Entity(name = "dynamic_ropes")
public class DynamicRope {
    
    private final String name;
    private final double diameter;
    private final int lengths;
    private final String type;
    private final double weight;

    public static class Builder {
        private final String name;
        private double diameter;
        private int lengths;
        private String type;
        private double weight;

        public Builder(String name) {
            this.name = name;
        }
        
        public Builder diameter(double val) {
            diameter = val;
            return this;
        }
        
        public Builder lengths(int val) {
            lengths = val;
            return this;
        }
        
        public Builder type(String val) {
            type = val;
            return this;
        }
        
        public Builder weight(double val) {
            weight = val;
            return this;
        }
        
        public DynamicRope build() {
            return new DynamicRope(this);    
        }
    }
    
    private DynamicRope(Builder builder) {
        this.name = builder.name;
        this.diameter = builder.diameter;
        this.lengths = builder.lengths;
        this.type = builder.type;
        this.weight = builder.weight;
    }
    
    @Column(name = "name")
    public String getName() {
        return name;
    }

    @Column(name = "diameter")
    public double getDiameter() {
        return diameter;
    }

    @Column(name = "lengths")
    public int getLengths() {
        return lengths;
    }

    @Column(name = "type")
    public String getType() {
        return type;
    }

    @Column(name = "weight")
    public double getWeight() {
        return weight;
    }
}