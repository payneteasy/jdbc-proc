package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import org.springframework.dao.DataAccessException;
import org.springframework.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * Inserts parameters to table
 */
public class ParametersSetterBlockList extends AbstractParametersSetterBlock {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    public ParametersSetterBlockList(String aInsertQuery, List<EntityArgumentGetter> aArgumentsGetters) {
        theInsertQuery = aInsertQuery;
        theArgumentsGetters = Collections.unmodifiableList(aArgumentsGetters);
    }

    public void setParameters(CallableStatement aStmt, Object[] aArgs) throws DataAccessException {
        if(LOG.isDebugEnabled()) {
            LOG.debug("Executing insert: {}", theInsertQuery);
        }
        Assert.notNull(aArgs         , "Argument aArgs must not be null"   );
        final Object[] arguments = skipNonCollectionArguments(aArgs);
        Assert.isTrue(arguments.length==1, "Count of argument must be equals 1");

        try {
            Connection con = aStmt.getConnection();
            List list = (List) arguments[0];
            for (Object entity : list) {
                PreparedStatement stmt = con.prepareStatement(theInsertQuery);
                try {
                    int index = 0;
                    for (EntityArgumentGetter getter : theArgumentsGetters) {
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

    public String toString() {
        return "ParametersSetterBlockList{" +
                "LOG=" + LOG +
                ", theInsertQuery='" + theInsertQuery + '\'' +
                ", theArgumentsGetters=" + theArgumentsGetters +
                '}';
    }

    private final String theInsertQuery;
    private final List<EntityArgumentGetter> theArgumentsGetters ;
}