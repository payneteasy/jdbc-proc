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

package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.googlecode.jdbcproc.daofactory.impl.block.IParametersSetterBlock;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementSetStrategy;

import java.sql.CallableStatement;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.util.Assert;

/**
 * Sets parameters to procedure from method's arguments as json string
 */
public class ParametersSetterBlockJson implements IParametersSetterBlock {

    private static final Gson GSON = new GsonBuilder().create();

    public ParametersSetterBlockJson(ArgumentGetter aArgumentsGetter,  int aListParameterIndex) {
        theArgumentsGetter = aArgumentsGetter;
        theListParameterIndex = aListParameterIndex;
    }

    public void setParameters(ICallableStatementSetStrategy aStmt, Object[] aMethodParameters)
            throws DataAccessException, SQLException {
        Assert.notNull(aMethodParameters, "Argument aArgs must not be null");
        Object value = aMethodParameters[theListParameterIndex];
        String json = (value != null) ? GSON.toJson(value) : null;
        theArgumentsGetter.setParameter(json, aStmt);
    }

    public void cleanup(CallableStatement aStmt) throws DataAccessException, SQLException {
    }

    public String toString() {
        return "ParametersSetterBlockArguments{" + "theArgumentsGetters=" + theArgumentsGetter + '}';
    }

    private final int theListParameterIndex;
    private final ArgumentGetter theArgumentsGetter;
}
