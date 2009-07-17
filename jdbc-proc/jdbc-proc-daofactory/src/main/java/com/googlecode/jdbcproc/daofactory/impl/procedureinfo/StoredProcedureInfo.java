package com.googlecode.jdbcproc.daofactory.impl.procedureinfo;

import java.util.List;
import java.util.LinkedList;
import java.util.Collections;

/**
 * Stored procedure info
 */
public class StoredProcedureInfo {

    public StoredProcedureInfo(String aName) {
        theName = aName;
    }

    protected void addColumn(StoredProcedureArgumentInfo aArgumentInfo) {
        theArguments.add(aArgumentInfo);
    }

    protected void addResultSetColumn(ResultSetColumnInfo aColumnInfo) {
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
    
    private final String theName;
    private final List<StoredProcedureArgumentInfo> theArguments = new LinkedList<StoredProcedureArgumentInfo>();
    private final List<ResultSetColumnInfo> theResultSetColumnInfos = new LinkedList<ResultSetColumnInfo>();
}
