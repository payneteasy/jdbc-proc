package com.googlecode.jdbcproc.daofactory.impl.block.impl;

/**
 * Parameter index and data type pair
 */
public class IndexDataTypePair {

    /**
     * Create index data type pair
     *
     * @param aIndex    index
     * @param aDataType sql data type
     */
    public IndexDataTypePair(int aIndex, int aDataType) {
        theDataType = aDataType;
        theIndex = aIndex;
    }

    public String toString() {
        return "IndexDataTypePair{" +
                "theDataType=" + theDataType +
                ", theIndex=" + theIndex +
                '}';
    }

    /** Index */
    public int getIndex() { return theIndex ; }

    /** data type */
    public int getDataType() { return theDataType ; }

    /** data type */
    private final int theDataType ;
    /** Index */
    private final int theIndex ;

}
