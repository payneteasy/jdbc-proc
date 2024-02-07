package com.googlecode.jdbcproc.daofactory.impl;

import javax.sql.DataSource;

import org.springframework.jdbc.core.CallableStatementCallback;

public abstract class CallableStatementCallbackWrapper<T> implements CallableStatementCallback<T> {

  private final DataSource dataSource;

  public CallableStatementCallbackWrapper(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  DataSource getDataSource() {
    return dataSource;
  }

}
