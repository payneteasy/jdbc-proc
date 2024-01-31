package com.googlecode.jdbcproc.daofactory.impl.procedureinfo;

import com.googlecode.jdbcproc.daofactory.impl.TypeNameUtil;

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
        theDataTypeName = TypeNameUtil.getName(theDataType);
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
     * @return data type of java.sql.Types.*
     */
    public int getDataType() {
        return theDataType;
    }

    /**
     * Data type name
     *
     * @return data type name of java.sql.Types.*
     */
    public String getDataTypeName() {
        return theDataTypeName;
    }

    public String toString() {
        return "ResultSetColumnInfo{" +
                "theDataType=" + theDataTypeName +
                ", theColumnName='" + theColumnName + '\'' +
                '}';
    }

    /**
     * Data type
     */
    private final int theDataType;

    /**
     * Data type name
     */
    private final String theDataTypeName;

    /**
     * Column name
     */
    private final String theColumnName;

}
