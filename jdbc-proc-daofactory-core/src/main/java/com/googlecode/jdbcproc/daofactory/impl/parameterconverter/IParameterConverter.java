package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementGetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementSetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementArgument;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

/**
 * Sets parameter
 */
public interface IParameterConverter<T extends IParameterConverter, V> {
  
  public static class Type<T extends IParameterConverter> {
    private final int sqlType;
    private final Class javaType;

    public Type(int sqlType) {
      this(sqlType, null);
    }
    
    public Type(int sqlType, Class javaType) {
      this.sqlType = sqlType;
      this.javaType = javaType;
    }

    @Override public int hashCode() {
      int result = sqlType;
      return result * 31 + (javaType != null ? javaType.hashCode() : 0);
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (!(obj instanceof Type)) {
        return false;
      }

      Type other = (Type) obj;
      if (sqlType != other.sqlType) {
        return false;
      }
      if (javaType == null) {
        return other.javaType == null;
      }
      return javaType.equals(other.javaType);
    }

    @Override public String toString() {
      return "Type[sqlType=" + sqlType + ", javaType=" + javaType + "]";
    }
  }
  
    /**
     * Sets parameter to statement
     *
     * @param aValue         value
     * @param aStmt          statement
     * @param aArgument
     * @throws SQLException  on error
     */
    void setValue(V aValue, ICallableStatementSetStrategy aStmt, StatementArgument aArgument) throws SQLException ;

    /**
     * Returns output parameter, converted to disired java type
     * @param aStmt          callable statement
     * @param aStatementArgument parameter name
     * @return               converted value
     * @throws SQLException on error
     */
    V getOutputParameter(ICallableStatementGetStrategy aStmt, StatementArgument aStatementArgument) throws SQLException;

    /**
     * Gets value from result set
     * @param aResultSet result set
     * @param aParameterName parameter name
     * @return converted value on error
     */
    V getFromResultSet(ResultSet aResultSet, String aParameterName) throws SQLException;


    /**
     * Key for finding
     * @return key
     */
    Type<T> getType();
}
