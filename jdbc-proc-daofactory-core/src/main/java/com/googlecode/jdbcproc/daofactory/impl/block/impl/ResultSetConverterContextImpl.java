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
    private Runnable          onCloseCleanup;

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

    public ResultSetConverterContextBuilder setOnCloseCleanup(Runnable onCloseCleanup) {
      this.onCloseCleanup = onCloseCleanup;
      return this;
    }

    public ResultSetConverterContextImpl build() {
      return new ResultSetConverterContextImpl(resultSet, callableStatement, dataSource, onCloseCleanup);
    }
  }

  private final ResultSet resultSet;
  private final CallableStatement callableStatement;
  private final DataSource dataSource;
  private final Runnable onCloseCleanup;

  private ResultSetConverterContextImpl(ResultSet resultSet, CallableStatement callableStatement,
                                        DataSource dataSource, Runnable onCloseCleanup) {
    this.resultSet         = resultSet;
    this.callableStatement = callableStatement;
    this.dataSource        = dataSource;
    this.onCloseCleanup    = onCloseCleanup;
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

  @Override public Runnable getOnCloseCleanup() {
    return onCloseCleanup;
  }

}
