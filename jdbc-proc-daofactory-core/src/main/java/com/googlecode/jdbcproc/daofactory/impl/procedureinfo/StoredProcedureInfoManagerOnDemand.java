package com.googlecode.jdbcproc.daofactory.impl.procedureinfo;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Returns procedure info on demand
 */
public class StoredProcedureInfoManagerOnDemand extends AbstractStoredProcedureInfoManager {

    /**
     * 
     * @param aDataSource
     * @throws SQLException
     */
    public StoredProcedureInfoManagerOnDemand(DataSource aDataSource) throws SQLException {
        theDataSource = aDataSource;
        Connection con = aDataSource.getConnection();
        try {
            // gets result set info
            Map<String, StoredProcedureInfo> map = new HashMap<String, StoredProcedureInfo>();
            putResultSetColumnsInfo(map, con);
            theProceduresMap = Collections.unmodifiableMap(map);
        } finally {
            commitIfNeededAndCloseForFinally(con);
        }
    }

    /**
     * {@inheritDoc}
     */
    public StoredProcedureInfo getProcedureInfo(String aProcedureName) {
        if(LOG.isDebugEnabled()) {
            LOG.debug("Getting info about {} procedure from database ...", aProcedureName);
        }
        try {
            Connection con = theDataSource.getConnection();

            try {
                StoredProcedureInfo procedureInfo = new StoredProcedureInfo(aProcedureName);

                DatabaseMetaData meta = con.getMetaData();

                // if procedures with arguments
                addArguments(procedureInfo, aProcedureName, con.getCatalog(), meta);

                // add result set
                if(theProceduresMap.containsKey(aProcedureName)) {
                    StoredProcedureInfo resultSetInfo = theProceduresMap.get(aProcedureName);
                    for (ResultSetColumnInfo columnInfo : resultSetInfo.getResultSetColumns()) {
                        procedureInfo.addResultSetColumn(columnInfo);
                    }
                }

                return procedureInfo;
            } finally {
                commitIfNeededAndCloseForFinally(con);
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Problem getting "+aProcedureName+" info : "+e.getMessage(), e);
        }
    }


    private void addArguments(StoredProcedureInfo aProcedureInfo, String aProcedureName, String aCatalog, DatabaseMetaData meta) throws SQLException {
        // gets procedures with arguments
        ResultSet rs = meta.getProcedureColumns(
                  aCatalog        // catalog
                , null            // schema
                , aProcedureName  // procedure name
                , "%"              // all columns
        );
        try {
            while (rs.next()) {
                // retrieves information about columns
                String procedureName = rs.getString(PROCEDURE_NAME);
                String columnName = rs.getString(COLUMN_NAME);
                short  columnType = rs.getShort(COLUMN_TYPE);

                // for ms sql
                if(columnName!=null && columnName.contains("@")) {
                    columnName = columnName.replace("@", "");
                }
                short  dataType   = rs.getShort(DATA_TYPE);

                // adds column info
                try {
                    aProcedureInfo.addColumn(columnName, columnType, dataType);
                } catch (Exception e) {
                    throw new IllegalStateException("Cannot create column info for procedure "+procedureName+": "+e.getMessage(), e);
                }
            }
        } finally {
            rs.close();
        }
    }


    private final Map<String, StoredProcedureInfo> theProceduresMap;

    /** JdbcTemplate */
    private final DataSource theDataSource;

}
