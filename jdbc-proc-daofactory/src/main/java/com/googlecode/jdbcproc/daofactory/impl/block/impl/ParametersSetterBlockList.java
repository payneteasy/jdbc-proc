package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.block.IParametersSetterBlock;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.impl.CallableStatementSetStrategyIndexImpl;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementSetStrategy;
import org.springframework.dao.DataAccessException;
import org.springframework.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Inserts parameters to table
 */
public class ParametersSetterBlockList implements IParametersSetterBlock {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    
    public ParametersSetterBlockList(String aInsertQuery, List<IEntityArgumentGetter> aArgumentsGetters,
            String aTruncateTableQuery, int aListParameterIndex) {
        theInsertQuery = aInsertQuery;
        theArgumentsGetters = Collections.unmodifiableList(aArgumentsGetters);
        theClearTableQuery = aTruncateTableQuery;
        theListParameterIndex = aListParameterIndex;
    }

    public void setParameters(ICallableStatementSetStrategy aStmt, Object[] aMethodParameters) throws DataAccessException {
        if(LOG.isDebugEnabled()) {
            LOG.debug("Executing insert: {}", theInsertQuery);
        }
        Assert.notNull(aMethodParameters, "Argument aArgs must not be null"   );
        Assert.isTrue(aMethodParameters.length > 0, "Count of arguments must be positive");

        try {
            Connection con = aStmt.getConnection();
            // delete previous data from table
            clearTable( con );

            // inserts current data to table
            Collection collection = (Collection) aMethodParameters[theListParameterIndex];

            PreparedStatement stmt = con.prepareStatement(theInsertQuery);
            ICallableStatementSetStrategy stmtStrategy = new CallableStatementSetStrategyIndexImpl(stmt);
            try {
                for (Object entity : collection) {
                    //int index = 0;
                    for (IEntityArgumentGetter getter : theArgumentsGetters) {
                        try {
                            //index++;
                            getter.setParameter(entity, stmtStrategy);
                        } catch (Exception e) {
                            throw new IllegalStateException("Error setting "+getter+": "+e.getMessage(),e);
                        }
                    }
                    stmt.addBatch();
                }
                stmt.executeBatch();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            } finally {
                stmt.close();
            }
        } catch(SQLException e) {
            throw new IllegalStateException(e);
        }
    }
    
    public void cleanup(CallableStatement aStmt) throws DataAccessException, SQLException {
         clearTable(aStmt.getConnection());
    }

    private void clearTable(Connection aConnection) throws SQLException {
        if(LOG.isDebugEnabled()) {
            LOG.debug("Executing clear: {}", theClearTableQuery);
        }
        Statement stmt = aConnection.createStatement();
        try {
            stmt.executeUpdate(theClearTableQuery);
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

    private final int theListParameterIndex;
    private final String theInsertQuery;
    private final String theClearTableQuery;
    private final List<IEntityArgumentGetter> theArgumentsGetters ;
}
