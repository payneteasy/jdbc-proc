package com.googlecode.jdbcproc.daofactory.impl.dbstrategy;

/**
 * For db strategy
 */
public class StatementArgument {

    public StatementArgument(String aParameterName, int aParameterIndex) {
        theParameterName = aParameterName;
        theParameterIndex = aParameterIndex;
    }

    /** Parameter index */
    public int getParameterIndex() { return theParameterIndex; }

    /** parameter name */
    public String getName() { return theParameterName; }

    /** Parameter index */
    private final int theParameterIndex;
    /** parameter name */
    private final String theParameterName;
}
