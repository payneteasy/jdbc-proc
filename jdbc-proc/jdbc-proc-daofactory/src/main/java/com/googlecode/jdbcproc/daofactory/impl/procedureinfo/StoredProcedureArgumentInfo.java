package com.googlecode.jdbcproc.daofactory.impl.procedureinfo;

/**
 * Stored procedure column info
 */
public class StoredProcedureArgumentInfo {

    public static final int OUT = 4;
    public static final int IN = 1;

    public StoredProcedureArgumentInfo(String aColumnName, short aColumnType, short aDataType) {
        theColumnName = aColumnName;
        theColumnType = aColumnType;
        theDataType = aDataType;
    }

    /**
     * Column name
     *
     * @return Column name
     */
    public String getColumnName() {
        return theColumnName;
    }

    /**
     * Column type
     * @return type of column
     */
    public short getColumnType() {
        return theColumnType;
    }

    /**
     * Data type
     * @return Types.*
     */
    public short getDataType() {
        return theDataType;
    }

    public boolean isOutputParameter() {
        return theColumnType==OUT;
    }

    public boolean isInputParameter() {
        return theColumnType == IN;
    }


    public String toString() {
        return "StoredProcedureArgumentInfo{" +
                "theColumnName='" + theColumnName + '\'' +
                ", theColumnType=" + theColumnType +
                ", theDataType=" + theDataType +
                '}';
    }

    private final String theColumnName;
    private final short theColumnType ;
    private final short theDataType;
}
