package com.googlecode.jdbcproc.daofactory.impl.procedureinfo;

import java.util.*;

/**
 * Stored procedure info
 */
public class StoredProcedureInfo {

    public StoredProcedureInfo(String aName) {
        theName = aName;
    }

    public void addColumn(String aColumnName, short aColumnType, short aDataType) {
        StoredProcedureArgumentInfo argumentInfo = new StoredProcedureArgumentInfo(theCurrentColumnIndex, aColumnName, aColumnType, aDataType);
        theCurrentColumnIndex++;

        theArguments.add(argumentInfo);
        String name = argumentInfo.getColumnName();
        if(name.startsWith("i_") || name.startsWith("o_")) {
            name = name.substring(2);
        }
        theArgumentsByNameMap.put(name, argumentInfo);
    }

    public void addResultSetColumn(ResultSetColumnInfo aColumnInfo) {
        theRsColumnByNameMap.put(aColumnInfo.getColumnName(), aColumnInfo);
        theResultSetColumnInfos.add(aColumnInfo);
    }
    
    public int getArgumentsCounts() {
        return theArguments.size();
    }

    /**
     * Input arguments count
     * 
     * @return count
     */
    public int getInputArgumentsCount() {
        int count = 0 ;
        for (StoredProcedureArgumentInfo argument : theArguments) {
            if(argument.isInputParameter()) {
                count++;
            }
        }
        return count;
    }
    
    public String getProcedureName() {
        return theName;
    }

    public List<StoredProcedureArgumentInfo> getInputArguments() {
        List<StoredProcedureArgumentInfo> arguments = new LinkedList<StoredProcedureArgumentInfo>();
        for (StoredProcedureArgumentInfo argument : theArguments) {
            if(argument.isInputParameter()) {
                arguments.add(argument);
            }
        }
        return Collections.unmodifiableList(arguments);
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


    public String toString() {
        return "StoredProcedureInfo{" +
                "theName='" + theName + '\'' +
                ", theArguments=" + theArguments +
                ", theResultSetColumnInfos=" + theResultSetColumnInfos +
                ", theArgumentsByNameMap=" + theArgumentsByNameMap +
                ", theRsColumnByNameMap=" + theRsColumnByNameMap +
                '}';
    }

    private final String theName;
    private final List<StoredProcedureArgumentInfo> theArguments = new LinkedList<StoredProcedureArgumentInfo>();
    private final List<ResultSetColumnInfo> theResultSetColumnInfos = new LinkedList<ResultSetColumnInfo>();
    private final Map<String, StoredProcedureArgumentInfo> theArgumentsByNameMap = new HashMap<String, StoredProcedureArgumentInfo>();
    private final Map<String, ResultSetColumnInfo> theRsColumnByNameMap = new HashMap<String, ResultSetColumnInfo>();
    private int theCurrentColumnIndex = 1;
}
