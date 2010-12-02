package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.block.IParametersSetterBlock;

import org.springframework.dao.DataAccessException;
import org.springframework.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Collections;
import java.util.List;

/**
 * Inserts parameters to table
 */
public class ParametersSetterBlockList implements IParametersSetterBlock {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    
    public ParametersSetterBlockList(String aInsertQuery, List<IEntityArgumentGetter> aArgumentsGetters,
      String aTrancateTableQuery) {
        theInsertQuery = aInsertQuery;
        theArgumentsGetters = Collections.unmodifiableList(aArgumentsGetters);
        theTruncateTableQuery = aTrancateTableQuery;
    }

    public void setParameters(CallableStatement aStmt, Object[] aArgs) throws DataAccessException {
        if(LOG.isDebugEnabled()) {
            LOG.debug("Executing insert: {}", theInsertQuery);
        }
        Assert.notNull(aArgs         , "Argument aArgs must not be null"   );
        Assert.isTrue(aArgs.length==1, "Count of argument must be equals 1");

        try {
            Connection con = aStmt.getConnection();
            // delete previous data from table
            truncateTable( con );

            // inserts current data to table
            List list = (List) aArgs[0];
            
            for (Object entity : list) {
                PreparedStatement stmt = con.prepareStatement(theInsertQuery);
                try {
                    int index = 0;
                    for (IEntityArgumentGetter getter : theArgumentsGetters) {
                        try {
                            index++;
                            getter.setParameterByIndex(entity, stmt, index);
                        } catch (Exception e) {
                            throw new IllegalStateException("Error setting "+getter.getParameterName()+": "+e.getMessage(),e);
                        }
                    }
                    stmt.executeUpdate();
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                } finally {
                    stmt.close();
                }
            }
        } catch(SQLException e) {
            throw new IllegalStateException(e);
        }
    }
    
    public void cleanup(CallableStatement aStmt) throws DataAccessException, SQLException {
         truncateTable(aStmt.getConnection());
    }

    private void truncateTable(Connection aConnection) throws SQLException {
        if(LOG.isDebugEnabled()) {
            LOG.debug("Executing truncate: {}", theTruncateTableQuery);
        }
        Statement stmt = aConnection.createStatement();
        try {
            stmt.executeUpdate(theTruncateTableQuery);
        } finally {
            stmt.close();
        }
    }

    public String toString() {
        return "ParametersSetterBlockList{" +
                "LOG=" + LOG +
                ", theInsertQuery='" + theInsertQuery + '\'' +
                ", theArgumentsGetters=" + theArgumentsGetters +
                '}';
    }

    private final String theInsertQuery;
    private final String theTruncateTableQuery;
    private final List<IEntityArgumentGetter> theArgumentsGetters ;
}
