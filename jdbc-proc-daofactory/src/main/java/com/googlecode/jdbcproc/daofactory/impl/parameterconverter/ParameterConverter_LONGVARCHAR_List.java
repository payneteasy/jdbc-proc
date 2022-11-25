/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementGetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementSetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementArgument;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

/**
 *  LONGVARCHAR - String
 */
public class ParameterConverter_LONGVARCHAR_List 
    implements IParameterConverter<ParameterConverter_LONGVARCHAR_List, List<?>> {

  public static final Type<ParameterConverter_LONGVARCHAR_List> TYPE 
      = new Type<ParameterConverter_LONGVARCHAR_List>(Types.LONGVARCHAR, List.class);

  private static final Gson GSON = new GsonBuilder().create();

  private final boolean isFilter3ByteChars;

    public ParameterConverter_LONGVARCHAR_List(boolean isFilter3ByteChars) {
        this.isFilter3ByteChars = isFilter3ByteChars;
    }

    public void setValue(List<?> aValue, ICallableStatementSetStrategy aStmt, StatementArgument aArgument) throws SQLException {
        String value = aValue != null ? GSON.toJson(aValue) : null;
        if (isFilter3ByteChars) {
            String fixedString = ParameterConverterUtils.filter3BytesUTF(value);
            aStmt.setString(aArgument, fixedString);
        } else {
            aStmt.setString(aArgument, value);
        }
    }

    public List getOutputParameter(ICallableStatementGetStrategy aStmt, StatementArgument aParameterName) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public List getFromResultSet(ResultSet aResultSet, String aParameterName) throws SQLException {
        throw new UnsupportedOperationException();
    }

  public Type<ParameterConverter_LONGVARCHAR_List> getType() {
    return TYPE;
  }

    public String toString() {
        return "ParameterConverter_LONGVARCHAR_String{}";
    }
}
