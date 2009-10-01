package com.googlecode.jdbcproc.daofactory.impl.procedureinfo;

import com.googlecode.jdbcproc.daofactory.impl.TypeNameUtil;

/**
 * Stored procedure column info
 */
public class StoredProcedureArgumentInfo {

    private static final int OUT = 4;
    private static final int IN  = 1;

    public StoredProcedureArgumentInfo(String aColumnName, short aColumnType, short aDataType) throws IllegalStateException {
        theColumnName = aColumnName;
        theColumnType = aColumnType;
        theDataType = aDataType;

        theDataTypeName = TypeNameUtil.getName(theDataType);
        switch(aColumnType) {
            case IN: theColumnTypeName = "IN"; break;
            case OUT: theColumnTypeName = "OUT"; break;
            default:
                throw new IllegalStateException("Column type "+aColumnType+" is not supported");
        }
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
     * Column type name (IN or OUT)
     * 
     * @return type name of column
     */
    public String getColumnTypeName() {
        return theColumnTypeName;
    }

    /**
     * Data type
     * @return Types.*
     */
    public short getDataType() {
        return theDataType;
    }

    /**
     * Data type name
     *
     * @return Types.* name
     */
    public String getDataTypeName() {
        return theDataTypeName;
    }

    public boolean isOutputParameter() {
        return theColumnType==OUT;
    }

    public boolean isInputParameter() {
        return theColumnType == IN;
    }


    public String toString() {
        return "StoredProcedureArgumentInfo{" +
                "columnName='" + theColumnName + '\'' +
                ", columnType=" + theColumnTypeName  +
                ", dataType=" + theDataTypeName +
                '}';
    }

    private final String theColumnName;
    private final short  theColumnType ;
    private final short  theDataType;
    
    private final String theDataTypeName;
    private final String theColumnTypeName;
}
