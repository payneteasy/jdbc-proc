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
import java.util.Date;

/**
 *  TIME - java.util.Date
 */
public class ParameterConverter_TIME_utilDate 
  implements IParameterConverter<ParameterConverter_TIME_utilDate, java.util.Date> {

  public static final Type<ParameterConverter_TIME_utilDate> TYPE 
      = new Type<ParameterConverter_TIME_utilDate>(Types.TIME, java.util.Date.class);
  
    public void setValue(Date value, PreparedStatement stmt, int index) throws SQLException {
        if(value!=null) {
            java.sql.Time time = new Time(value.getTime());
            stmt.setTime(index, time);
        } else {
            stmt.setNull(index, Types.TIME);
        }
    }

    public void setValue(Date value, ICallableStatementSetStrategy stmt, StatementArgument parameterName)
      throws SQLException {
        if(value!=null) {
            java.sql.Time time = new Time(value.getTime());
            stmt.setTime(parameterName, time);
        } else {
            stmt.setNull(parameterName, Types.TIME);
        }
    }

    public java.util.Date getOutputParameter(ICallableStatementGetStrategy aStmt, StatementArgument aParameterName)
      throws SQLException {
        return aStmt.getTime(aParameterName);
    }

    public Date getFromResultSet(ResultSet resultSet, String parameterName) throws SQLException {
        return resultSet.getTime(parameterName);
    }

    public Type<ParameterConverter_TIME_utilDate> getType() {
        return TYPE;
    }

    public String toString() {
        return "ParameterConverter_TIME_utilDate{}";
    }
}
