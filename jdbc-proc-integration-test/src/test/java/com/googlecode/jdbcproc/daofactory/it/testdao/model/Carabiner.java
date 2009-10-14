package com.googlecode.jdbcproc.daofactory.it.testdao.model;

import javax.persistence.Entity;
import javax.persistence.Column;

@Entity(name = "carabiners")
public class Carabiner {
    
    private final String name;
    private final double weight;
    private final int breakingStrengthMajorAxis;
    private final int breakingStrengthOpenGate;
    private final int breakingStrengthCrossLoaded;
    private final int gateOpening;

    public static class Builder {
        private final String name;
        private final double weight;
        private int breakingStrengthMajorAxis;
        private int breakingStrengthOpenGate;
        private int breakingStrengthCrossLoaded;
        private int gateOpening;

        public Builder(String name, double weight) {
            this.name = name;
            this.weight = weight;
        }

        public Builder breakingStrengthMajorAxis(int val) {
            breakingStrengthMajorAxis = val;
            return this;
        }
        
        public Builder breakingStrengthOpenGate(int val) {
            breakingStrengthOpenGate = val;
            return this;
        }
        
        public Builder breakingStrengthCrossLoaded(int val) {
            breakingStrengthCrossLoaded = val;
            return this;
        }
        
        public Builder gateOpening(int val) {
            gateOpening = val;
            return this;
        }
        
        public Carabiner build() {
            return new Carabiner(this);
        }
    }
    
    private Carabiner(Builder builder) {
        this.name = builder.name;
        this.weight = builder.weight;
        this.breakingStrengthMajorAxis = builder.breakingStrengthMajorAxis;
        this.breakingStrengthOpenGate = builder.breakingStrengthOpenGate;
        this.breakingStrengthCrossLoaded = builder.breakingStrengthCrossLoaded;
        this.gateOpening = builder.gateOpening;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    @Column(name = "weight")
    public double getWeight() {
        return weight;
    }

    @Column(name = "bs_major_axis")
    public int getBreakingStrengthMajorAxis() {
        return breakingStrengthMajorAxis;
    }

    @Column(name = "bs_open_gate")
    public int getBreakingStrengthOpenGate() {
        return breakingStrengthOpenGate;
    }

    @Column(name = "bs_cross_loaded")
    public int getBreakingStrengthCrossLoaded() {
        return breakingStrengthCrossLoaded;
    }

    @Column(name = "gate_opening")
    public int getGateOpening() {
        return gateOpening;
    }
}