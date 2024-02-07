package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.block.IResultSetConverterContext;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import javax.sql.DataSource;

public class ResultSetConverterContextImpl implements IResultSetConverterContext {

  public static class ResultSetConverterContextBuilder {

    private ResultSet resultSet;
    private CallableStatement callableStatement;
    private DataSource        dataSource;

    public ResultSetConverterContextBuilder setResultSet(ResultSet resultSet) {
      this.resultSet = resultSet;
      return this;
    }

    public ResultSetConverterContextBuilder setCallableStatement(CallableStatement callableStatement) {
      this.callableStatement = callableStatement;
      return this;
    }

    public ResultSetConverterContextBuilder setDataSource(DataSource dataSource) {
      this.dataSource = dataSource;
      return this;
    }

    public ResultSetConverterContextImpl build() {
      return new ResultSetConverterContextImpl(resultSet, callableStatement, dataSource);
    }
  }

  private final ResultSet resultSet;
  private final CallableStatement callableStatement;
  private final DataSource dataSource;

  private ResultSetConverterContextImpl(ResultSet resultSet, CallableStatement callableStatement, DataSource dataSource) {
    this.resultSet         = resultSet;
    this.callableStatement = callableStatement;
    this.dataSource        = dataSource;
  }

  public static ResultSetConverterContextBuilder builder() {
    return new ResultSetConverterContextBuilder();
  }

  @Override public ResultSet getResultSet() {
    return resultSet;
  }

  @Override public CallableStatement getCallableStatement() {
    return callableStatement;
  }

  @Override public DataSource getDataSource() {
    return dataSource;
  }

}
