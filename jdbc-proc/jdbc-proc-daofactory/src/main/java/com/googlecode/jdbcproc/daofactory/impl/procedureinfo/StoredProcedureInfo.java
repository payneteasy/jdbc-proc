package com.googlecode.jdbcproc.daofactory.impl.procedureinfo;

import java.util.*;

/**
 * Stored procedure info
 */
public class StoredProcedureInfo {

    public StoredProcedureInfo(String aName) {
        theName = aName;
    }

    protected void addColumn(StoredProcedureArgumentInfo aArgumentInfo) {
        theArguments.add(aArgumentInfo);
        String name = aArgumentInfo.getColumnName();
        if(name.startsWith("i_") || name.startsWith("o_")) {
            name = name.substring(2);
        }
        theArgumentsByNameMap.put(name, aArgumentInfo);
    }

    protected void addResultSetColumn(ResultSetColumnInfo aColumnInfo) {
        theRsColumnByNameMap.put(aColumnInfo.getColumnName(), aColumnInfo);
        theResultSetColumnInfos.add(aColumnInfo);
    }
    
    public int getArgumentsCounts() {
        return theArguments.size();
    }

    public String getProcedureName() {
        return theName;
    }

    public List<StoredProcedureArgumentInfo> getArguments() {
        return Collections.unmodifiableList(theArguments);
    }

    public List<ResultSetColumnInfo> getResultSetColumns() {
        return Collections.unmodifiableList(theResultSetColumnInfos);
    }
    
    public StoredProcedureArgumentInfo getArgumentInfo(String aColumnName) {
        return theArgumentsByNameMap.get(aColumnName);
    }

    public ResultSetColumnInfo getResultSetColumn(String aColumnName) {
        return theRsColumnByNameMap.get(aColumnName);
    }

    private final String theName;
    private final List<StoredProcedureArgumentInfo> theArguments = new LinkedList<StoredProcedureArgumentInfo>();
    private final List<ResultSetColumnInfo> theResultSetColumnInfos = new LinkedList<ResultSetColumnInfo>();
    private final Map<String, StoredProcedureArgumentInfo> theArgumentsByNameMap = new HashMap<String, StoredProcedureArgumentInfo>();
    private final Map<String, ResultSetColumnInfo> theRsColumnByNameMap = new HashMap<String, ResultSetColumnInfo>();

}
