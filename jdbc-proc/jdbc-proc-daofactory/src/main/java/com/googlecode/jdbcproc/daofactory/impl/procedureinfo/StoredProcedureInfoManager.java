package com.googlecode.jdbcproc.daofactory.impl.procedureinfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import java.sql.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Information about stored procedures
 */
public class StoredProcedureInfoManager {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    public StoredProcedureInfoManager(JdbcTemplate aJdbcTemplate) throws Exception {
        LOG.info("Creating dao columns cache map...");

        theNameTypeMap = Collections.unmodifiableMap(createNameTypeMap());

        Map<String, StoredProcedureInfo> map = new HashMap<String, StoredProcedureInfo>();
        Connection con = aJdbcTemplate.getDataSource().getConnection();
        try {
            DatabaseMetaData meta = con.getMetaData();

            // gets procedures with arguments
            ResultSet rs = meta.getProcedureColumns(
                    con.getCatalog()  // catalog
                    , null            // schema
                    , "%"             // all procedures
                    , null            // all columns
            );
            try {
                while (rs.next()) {
                    // retrieves information about columns
                    String procedureName = rs.getString("PROCEDURE_NAME");
                    String columnName = rs.getString("COLUMN_NAME");
                    short  columnType = rs.getShort("COLUMN_TYPE");
                    short  dataType   = rs.getShort("DATA_TYPE");

                    StoredProcedureInfo procedureInfo = findOrCreateStoredProcedure(map, procedureName);

                    // adds column info
                    procedureInfo.addColumn(new StoredProcedureArgumentInfo(columnName, columnType, dataType));
                }
            } finally {
                rs.close();
            }

            // gets procedures info without arguments
            rs = meta.getProcedures(
                    con.getCatalog() // catalog
                    , null           // schema
                    , "%"            // all procedures
            );
            try {
                while(rs.next()) {
                    String procedureName = rs.getString("PROCEDURE_NAME");
                    findOrCreateStoredProcedure(map, procedureName);
                }
            } finally {
                rs.close();
            }

            // gets result set info
            putResultSetColumnsInfo(map, con);

            // prints stored procedures info
            if(LOG.isDebugEnabled()) {
                printToLogStoredProceduresInfos(map);
            }
            theProceduresMap = Collections.unmodifiableMap(map);
        } finally {
            con.close();
        }

    }

    private void printToLogStoredProceduresInfos(Map<String, StoredProcedureInfo> aMap) {
        LOG.debug("Stored procedures list:");
        for (Map.Entry<String, StoredProcedureInfo> entry : aMap.entrySet()) {
            StoredProcedureInfo proc = entry.getValue();
            LOG.debug("Procedure {} [ name={}, arguments={}, results={} ]"
                    , new Object[] {entry.getKey(), proc.getProcedureName(), proc.getArgumentsCounts(), proc.getResultSetColumns().size()});

            // SHOW ARGUMENTS
            if(proc.getArguments().size()>0) {
                LOG.debug("  Arguments:");
                for (StoredProcedureArgumentInfo argumentInfo : proc.getArguments()) {
                    LOG.debug("    {} [ columnType={}, dataType={} ]"
                            , new Object[] {argumentInfo.getColumnName(), argumentInfo.getColumnType(), argumentInfo.getDataType()});
                }
            }

            // SHOW RESULT SET
            if(proc.getResultSetColumns().size()>0) {
                LOG.debug("  Results:");
                for (ResultSetColumnInfo columnInfo : proc.getResultSetColumns()) {
                    LOG.debug("    {} [ dataType={} ]", columnInfo.getColumnName(), columnInfo.getDataType());

                }
            }
        }
    }

    private Map<String, Integer> createNameTypeMap() {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("varchar"   , Types.VARCHAR);
        map.put("char"      , Types.VARCHAR);
        map.put("int"       , Types.INTEGER);
        map.put("smallint"  , Types.INTEGER);
        map.put("datetime"  , Types.TIMESTAMP);
        map.put("date"      , Types.DATE);
        map.put("decimal"   , Types.DECIMAL);
        map.put("blob"      , Types.LONGVARBINARY);
        return map;
    }

    private StoredProcedureInfo findOrCreateStoredProcedure(Map<String, StoredProcedureInfo> map, String procedureName) {
        // gets or creates procedure info
        StoredProcedureInfo procedureInfo = map.get(procedureName);
        if (procedureInfo == null) {
            procedureInfo = new StoredProcedureInfo(procedureName);
            map.put(procedureName, procedureInfo);
        }
        return procedureInfo;
    }

    private void putResultSetColumnsInfo(Map<String, StoredProcedureInfo> aMap, Connection aCon) throws SQLException {
        LOG.info("Calling get_procedures_resultset()...");
        CallableStatement stmt = aCon.prepareCall("{call get_procedures_resultset()}");
        try {
            ResultSet rs = stmt.executeQuery();
            try {
                while(rs.next())  {
                    String procedureName     = rs.getString("specific_name");
                    String routine_resultset = rs.getString("routine_resultset");
                    StoredProcedureInfo procedureInfo = findOrCreateStoredProcedure(aMap, procedureName);
                    if(StringUtils.hasText(routine_resultset)) {
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

    private void addResultSetColumns(StoredProcedureInfo aProcedure, String aLine) {
        if(LOG.isDebugEnabled()) {
            LOG.debug("Creating column info for {}()", aProcedure.getProcedureName());
        }
        StringTokenizer st = new StringTokenizer(aLine, ",;.");
        while(st.hasMoreTokens()) {
            StringTokenizer pairSt = new StringTokenizer(st.nextToken(), " \t");
            if(pairSt.hasMoreTokens()) {
                String columnName = pairSt.nextToken();
                if(pairSt.hasMoreTokens()) {
                    String typeName = pairSt.nextToken();
                    ResultSetColumnInfo columnInfo = new ResultSetColumnInfo(columnName, findDataType(typeName));
                    if(LOG.isDebugEnabled()) {
                        LOG.debug("    {} - {}", columnName, typeName);
                    }
                    aProcedure.addResultSetColumn(columnInfo);
                }
            }
        }
    }

    private int findDataType(String aTypeName) {
        Integer type = theNameTypeMap.get(aTypeName);
        if(type==null) throw new IllegalStateException("Type "+aTypeName+" is not supported");
        return type;
    }

    public StoredProcedureInfo getProcedureInfo(String aProcedureName) {
        return theProceduresMap.get(aProcedureName);
    }

    private final Map<String, Integer> theNameTypeMap;
    private final Map<String, StoredProcedureInfo> theProceduresMap;

}
