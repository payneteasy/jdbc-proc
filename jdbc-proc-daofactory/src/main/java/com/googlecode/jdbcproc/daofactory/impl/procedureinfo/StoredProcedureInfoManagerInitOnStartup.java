package com.googlecode.jdbcproc.daofactory.impl.procedureinfo;

import com.google.inject.Inject;

import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Information about stored procedures retrieves on startup
 */
public class StoredProcedureInfoManagerInitOnStartup extends AbstractStoredProcedureInfoManager {
    
  @Inject
    public StoredProcedureInfoManagerInitOnStartup(JdbcTemplate jdbcTemplate) throws Exception {
        LOG.info("Creating dao columns cache map...");

        Map<String, StoredProcedureInfo> map = new HashMap<String, StoredProcedureInfo>();
        Connection con = jdbcTemplate.getDataSource().getConnection();

        try {
            DatabaseMetaData meta = con.getMetaData();
            String catalog = con.getCatalog();

            // gets procedures with arguments
            getProceduresWithArguments(map, catalog, meta);

            // gets procedures info without arguments
            getProceduresWithoutArguments(map, catalog, meta);

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

    private void getProceduresWithoutArguments(Map<String, StoredProcedureInfo> map, String aCatalog, DatabaseMetaData meta) throws SQLException {
        // gets procedures info without arguments
        ResultSet rs2 = meta.getProcedures(
                  aCatalog       // catalog
                , null           // schema
                , "%"            // all procedures
        );
        try {
            while(rs2.next()) {
                String procedureName = rs2.getString(PROCEDURE_NAME);
                findOrCreateStoredProcedure(map, procedureName);
            }
        } finally {
            rs2.close();
        }
    }

    private void getProceduresWithArguments(Map<String, StoredProcedureInfo> map, String aCatalog, DatabaseMetaData meta) throws SQLException {
        // gets procedures with arguments
        ResultSet rs = meta.getProcedureColumns(
                  aCatalog        // catalog
                , null            // schema
                , "%"             // all procedures
                , "%"             // all columns
        );
        try {
            while (rs.next()) {
                // retrieves information about columns
                String procedureName = rs.getString(PROCEDURE_NAME);
                String columnName = rs.getString(COLUMN_NAME);
                short  columnType = rs.getShort(COLUMN_TYPE);
                short  dataType   = rs.getShort(DATA_TYPE);

                StoredProcedureInfo procedureInfo = findOrCreateStoredProcedure(map, procedureName);

                // adds column info
                try {
                    procedureInfo.addColumn(columnName, columnType, dataType);
                } catch (Exception e) {
                    throw new IllegalStateException("Cannot create column info for procedure "+procedureName+": "+e.getMessage(), e);
                }
            }
        } finally {
            rs.close();
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

    /**
     * {@inheritDoc}
     */
    public StoredProcedureInfo getProcedureInfo(String aProcedureName) {
        return theProceduresMap.get(aProcedureName);
    }

    private Map<String, StoredProcedureInfo> theProceduresMap;
}
