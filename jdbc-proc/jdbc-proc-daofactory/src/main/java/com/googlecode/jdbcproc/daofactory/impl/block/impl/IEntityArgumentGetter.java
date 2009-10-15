package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by IntelliJ IDEA.
 * User: esinev
 * Date: 16.10.2009
 * Time: 2:56:21
 * To change this template use File | Settings | File Templates.
 */
public interface IEntityArgumentGetter {
    void setParameter(Object aEntity, CallableStatement aStmt) throws InvocationTargetException, IllegalAccessException, SQLException;

    String getParameterName();

    void setParameterByIndex(Object aEntity, PreparedStatement aStmt, int aIndex) throws InvocationTargetException, IllegalAccessException, SQLException;
}
