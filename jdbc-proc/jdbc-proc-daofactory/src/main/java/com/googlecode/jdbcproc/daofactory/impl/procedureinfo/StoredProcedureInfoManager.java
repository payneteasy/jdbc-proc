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
        theJdbcTemplate = aJdbcTemplate;
        theNameTypeMap = Collections.unmodifiableMap(createNameTypeMap());
    }


    public void fillProcedureInfo(String procedureName) throws Exception {

        Map<String, StoredProcedureInfo> map = new HashMap<String, StoredProcedureInfo>();
        Connection con = theJdbcTemplate.getDataSource().getConnection();

        try {
            DatabaseMetaData meta = con.getMetaData();

            // gets procedures with arguments
            ResultSet rs = meta.getProcedureColumns(
                    con.getCatalog()  // catalog
                    , null            // schema
                    , procedureName   // all procedures
                    , null            // all columns
            );

            StoredProcedureInfo procedureInfo = findOrCreateStoredProcedure(map, procedureName);

            try {
                while (rs.next()) {
                    // retrieves information about columns
                    String columnName = rs.getString("COLUMN_NAME");
                    short columnType = rs.getShort("COLUMN_TYPE");
                    short dataType = rs.getShort("DATA_TYPE");

                    // adds column info
                    try {
                        procedureInfo.addColumn(new StoredProcedureArgumentInfo(columnName, columnType, dataType));
                    } catch (Exception e) {
                        throw new IllegalStateException("Cannot create column info for procedure " + procedureName + ": " + e.getMessage(), e);
                    }
                }
            } finally {
                rs.close();
            }

            // gets result set info
            putResultSetColumnsInfo(map, con);

            // prints stored procedures info
            if (LOG.isDebugEnabled()) {
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
                    , new Object[]{entry.getKey(), proc.getProcedureName(), proc.getArgumentsCounts(), proc.getResultSetColumns().size()});

            // SHOW ARGUMENTS
            if (proc.getArguments().size() > 0) {
                LOG.debug("  Arguments:");
                for (StoredProcedureArgumentInfo argumentInfo : proc.getArguments()) {
                    LOG.debug("    {} [ columnType={}, dataType={} ]"
                            , new Object[]{argumentInfo.getColumnName(), argumentInfo.getColumnTypeName(), argumentInfo.getDataTypeName()});
                }
            }

            // SHOW RESULT SET
            if (proc.getResultSetColumns().size() > 0) {
                LOG.debug("  Results:");
                for (ResultSetColumnInfo columnInfo : proc.getResultSetColumns()) {
                    LOG.debug("    {} [ dataType={} ]", columnInfo.getColumnName(), columnInfo.getDataTypeName());

                }
            }
        }
    }

    private Map<String, Integer> createNameTypeMap() {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("varchar", Types.VARCHAR);
        map.put("char", Types.VARCHAR);
        map.put("bigint", Types.BIGINT);
        map.put("int", Types.INTEGER);
        map.put("smallint", Types.INTEGER);
        map.put("datetime", Types.TIMESTAMP);
        map.put("date", Types.DATE);
        map.put("decimal", Types.DECIMAL);
        map.put("blob", Types.LONGVARBINARY);
        map.put("mediumblob", Types.LONGVARBINARY);
        map.put("longblob", Types.LONGVARBINARY);
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

    private void addResultSetColumns(StoredProcedureInfo aProcedure, String aLine) {
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

    private int findDataType(String aTypeName) {
        Integer type = theNameTypeMap.get(aTypeName);
        if (type == null) throw new IllegalStateException("Type " + aTypeName + " is not supported");
        return type;
    }

    public StoredProcedureInfo getProcedureInfo(String aProcedureName) {
        try {
            fillProcedureInfo(aProcedureName);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return theProceduresMap.get(aProcedureName);
    }

    private final Map<String, Integer> theNameTypeMap;
    private Map<String, StoredProcedureInfo> theProceduresMap;
    private final JdbcTemplate theJdbcTemplate;
}
