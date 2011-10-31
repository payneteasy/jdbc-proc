package com.googlecode.jdbcproc.daofactory.impl.dbstrategy;

/**
 * For db strategy
 */
public class StrategyParameter {

    public StrategyParameter(String aParameterName, String aParameterIndex) {
        theParameterName = aParameterName;
        theParameterIndex = aParameterIndex;
    }

    /** Parameter index */
    public String getParameterIndex() { return theParameterIndex; }

    /** Parameter index */
    private final String theParameterIndex;

    /** parameter name */
    public String getParameterName() { return theParameterName; }
    /** parameter name */
    private final String theParameterName;
}
