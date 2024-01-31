package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementSetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementArgument;
import com.googlecode.jdbcproc.daofactory.impl.parameterconverter.IParameterConverter;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

/**
 * Gets parameter from Entity and set to Callable Statement
 */
public class EntityArgumentGetter implements IEntityArgumentGetter {

    public EntityArgumentGetter(Method aGetterMethod, IParameterConverter aParameterConverter, StatementArgument aStatementArgument) {
        theMethod = aGetterMethod;
        theParameterConverter = aParameterConverter;
        theStatementArgument = aStatementArgument;
    }

    public void setParameter(Object aEntity, ICallableStatementSetStrategy aStmt) throws InvocationTargetException, IllegalAccessException, SQLException {
        Object value = theMethod.invoke(aEntity);
        theParameterConverter.setValue(value, aStmt, theStatementArgument);
    }

    public String getColumnNameForInsertQuery() {
        return theStatementArgument.getName();
//        todo insert column name
    }
    
    public String toString() {
        return "IEntityArgumentGetter{" +
                "theMethod=" + theMethod +
                ", theParameterConverter=" + theParameterConverter +
                ", theStatementArgument='" + theStatementArgument + '\'' +
                '}';
    }

    protected final Method theMethod;
    protected final IParameterConverter theParameterConverter;
    protected final StatementArgument theStatementArgument;

}
