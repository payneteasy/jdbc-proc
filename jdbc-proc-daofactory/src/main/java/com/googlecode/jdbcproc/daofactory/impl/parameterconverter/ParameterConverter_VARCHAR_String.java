package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementGetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementSetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementArgument;

import java.sql.*;
import java.util.stream.Collectors;

/**
 *  VARCHAR - String
 */
public class ParameterConverter_VARCHAR_String 
    implements IParameterConverter<ParameterConverter_VARCHAR_String, String> {

    private final boolean isFilter3ByteChars;

    public ParameterConverter_VARCHAR_String(boolean isFilter3ByteChars) {
        this.isFilter3ByteChars = isFilter3ByteChars;
    }

    public static final Type<ParameterConverter_VARCHAR_String> TYPE
      = new Type<ParameterConverter_VARCHAR_String>(Types.VARCHAR, String.class);

    public void setValue(String aValue, ICallableStatementSetStrategy aStmt, StatementArgument aArgument) throws SQLException {
        if (isFilter3ByteChars) {
            String fixedString = filter3BytesUTF(aValue);
            aStmt.setString(aArgument, fixedString);
        } else {
            aStmt.setString(aArgument, aValue);
        }
    }

    public String getOutputParameter(ICallableStatementGetStrategy aStmt, StatementArgument aParameterName) throws SQLException {
        return aStmt.getString(aParameterName);
    }

    public String getFromResultSet(ResultSet aResultSet, String aParameterName) throws SQLException {
        return aResultSet.getString(aParameterName);
    }

  public Type<ParameterConverter_VARCHAR_String> getType() {
    return TYPE;
  }

    public String toString() {
        return "ParameterConverter_VARCHAR_String{}";
    }

    static String filter3BytesUTF(String aValue) {
        if (aValue == null) {
            return null;
        }
        return aValue.codePoints()
                .filter(c -> c <= 0xFFFF)
                .mapToObj(e -> new String(new int[]{e}, 0, 1))
                .collect(Collectors.joining());
    }
}
