/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementGetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementSetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementArgument;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Types;

/**
 *  TIME - java.sql.Time
 */
public class ParameterConverter_TIME_Time 
  implements IParameterConverter<ParameterConverter_TIME_Time, Time> {

  public static final Type<ParameterConverter_TIME_Time> TYPE 
      = new Type<ParameterConverter_TIME_Time>(Types.TIME, Time.class);
  
    public void setValue(Time value, PreparedStatement stmt, int index) throws
      SQLException {
        if(value!=null) {
            stmt.setTime(index, value);
        } else {
            stmt.setNull(index, Types.TIME);
        }
    }

    public void setValue(Time value, ICallableStatementSetStrategy stmt, StatementArgument parameterName)
      throws SQLException {
        if(value!=null) {
            stmt.setTime(parameterName, value);
        } else {
            stmt.setNull(parameterName, Types.TIME);
        }
    }

    public Time getOutputParameter(ICallableStatementGetStrategy aStmt, StatementArgument aParameterName)
      throws SQLException {
        return aStmt.getTime(aParameterName);
    }

    public Time getFromResultSet(ResultSet resultSet, String parameterName) throws SQLException {
        return resultSet.getTime(parameterName);
    }

    public Type<ParameterConverter_TIME_Time> getType() {
        return TYPE;
    }
  
    public String toString() {
        return "ParameterConverter_TIME_Time{}";
    }
}
