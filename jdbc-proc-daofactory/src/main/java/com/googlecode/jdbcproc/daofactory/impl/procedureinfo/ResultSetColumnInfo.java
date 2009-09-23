package com.googlecode.jdbcproc.daofactory.impl.procedureinfo;

/**
 * Result set column info
 */
public class ResultSetColumnInfo {

    /**
     * Creates column info
     *
     * @param aColumnName column name
     * @param aDataType   data type
     */
    public ResultSetColumnInfo(String aColumnName, int aDataType) {
        theColumnName = aColumnName;
        theDataType = aDataType;
    }

    /**
     * Column name
     *
     * @return column name
     */
    public String getColumnName() {
        return theColumnName;
    }

    /**
     * Data type
     *
     * @return data type of java.sql.Typea.*
     */
    public int getDataType() {
        return theDataType;
    }


    public String toString() {
        return "ResultSetColumnInfo{" +
                "theDataType=" + theDataType +
                ", theColumnName='" + theColumnName + '\'' +
                '}';
    }

    /**
     * Data type
     */
    private final int theDataType;
    /**
     * Column name
     */
    private final String theColumnName;

}
