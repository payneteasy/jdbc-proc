package com.googlecode.jdbcproc.daofactory.impl.block;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import javax.sql.DataSource;

public interface IResultSetConverterContext {

  ResultSet getResultSet();

  CallableStatement getCallableStatement();

  DataSource getDataSource();

  /**
   * Cleanup to run when an iterator-returning result is closed — after the
   * streaming ResultSet has been read/closed, before the connection is released.
   * Used to defer parameter-setter cleanup (clearing the List-parameter temp
   * table) so it does not issue an executeUpdate while the streaming cursor is
   * still open; on the MariaDB driver that would force the whole result set to
   * be buffered into memory. {@code null} for non-iterator results.
   */
  Runnable getOnCloseCleanup();

}
