package com.googlecode.jdbcproc.daofactory.impl.procedureinfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.sql.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * 
 */
public abstract class AbstractStoredProcedureInfoManager implements IStoredProcedureInfoManager {

    static final String PROCEDURE_NAME = "PROCEDURE_NAME" ;
    static final String COLUMN_NAME    = "COLUMN_NAME"    ;
    static final String COLUMN_TYPE    = "COLUMN_TYPE"    ;
    static final String DATA_TYPE      = "DATA_TYPE"      ;

    final Logger LOG = LoggerFactory.getLogger(getClass());

    public AbstractStoredProcedureInfoManager() {
        theNameTypeMap = Collections.unmodifiableMap(createNameTypeMap());

    }

    /**
     * Finds data type mapping
     * @param aTypeName type name
     * @return data type
     */
    int findDataType(String aTypeName) {
        Integer type = theNameTypeMap.get(aTypeName);
        if (type == null) throw new IllegalStateException("Type " + aTypeName + " is not supported");
        return type;
    }


    StoredProcedureInfo findOrCreateStoredProcedure(Map<String, StoredProcedureInfo> map, String procedureName) {
        // gets or creates procedure info
        StoredProcedureInfo procedureInfo = map.get(procedureName);
        if (procedureInfo == null) {
            procedureInfo = new StoredProcedureInfo(procedureName);
            map.put(procedureName, procedureInfo);
        }
        return procedureInfo;
    }

    void putResultSetColumnsInfo(Map<String, StoredProcedureInfo> aMap, Connection aCon) throws SQLException {
        LOG.info("Calling get_procedures_resultset()...");
        CallableStatement stmt = aCon.prepareCall("{call get_procedures_resultset()}");
        try {
            ResultSet rs = stmt.executeQuery();
            try {
                while (rs.next()) {
                    String procedureName = rs.getString("specific_name");
                    String routine_resultset = rs.getString("routine_resultset");
                    StoredProcedureInfo procedureInfo = findOrCreateStoredProcedure(aMap, procedureName);
                    if (StringUtils.hasText(routine_resultset)) {
                        addResultSetColumns(procedureInfo, routine_resultset);
                    }
                }
            } finally {
                rs.close();
            }
        } finally {
            stmt.close();
        }
    }


    void addResultSetColumns(StoredProcedureInfo aProcedure, String aLine) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Creating column info for {}()", aProcedure.getProcedureName());
        }
        StringTokenizer st = new StringTokenizer(aLine, ",;.");
        while (st.hasMoreTokens()) {
            StringTokenizer pairSt = new StringTokenizer(st.nextToken(), " \t");
            if (pairSt.hasMoreTokens()) {
                String columnName = pairSt.nextToken();
                if (pairSt.hasMoreTokens()) {
                    String typeName = pairSt.nextToken();
                    ResultSetColumnInfo columnInfo = new ResultSetColumnInfo(columnName, findDataType(typeName));
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("    {} - {}", columnName, typeName);
                    }
                    aProcedure.addResultSetColumn(columnInfo);
                }
            }
        }
    }



    private Map<String, Integer> createNameTypeMap() {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("varchar"    , Types.VARCHAR);
        map.put("char"       , Types.VARCHAR);
        map.put("bigint"     , Types.BIGINT);
        map.put("int"        , Types.INTEGER);
        map.put("integer"    , Types.INTEGER);
        map.put("smallint"   , Types.INTEGER);
        map.put("datetime"   , Types.TIMESTAMP);
        map.put("date"       , Types.DATE);
        map.put("decimal"    , Types.DECIMAL);
        map.put("blob"       , Types.LONGVARBINARY);
        map.put("mediumblob" , Types.LONGVARBINARY);
        map.put("longblob"   , Types.LONGVARBINARY);
        return map;
    }

    protected void commitIfNeededAndCloseForFinally(Connection con) throws SQLException {
        try {
            if (!con.getAutoCommit()) {
                // only committing if autocommit=OFF as otherwise it's not needed and causes an exception
                con.commit();
            }
        } catch (SQLException e) {
            // ignoring, just logging
            LOG.error("Error while committing connection (before closing)", e);
        } finally {
            con.close();
        }
    }

    private final Map<String, Integer> theNameTypeMap;

}
