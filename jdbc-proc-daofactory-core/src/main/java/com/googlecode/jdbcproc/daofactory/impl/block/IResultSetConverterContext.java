package com.googlecode.jdbcproc.daofactory.impl.block;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import javax.sql.DataSource;

public interface IResultSetConverterContext {

  ResultSet getResultSet();

  CallableStatement getCallableStatement();

  DataSource getDataSource();

}
