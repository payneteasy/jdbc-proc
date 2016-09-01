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

import com.googlecode.jdbcproc.daofactory.impl.TypeNameUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ParameterConverterServiceImpl implements ParameterConverterService {

  private final Map<IParameterConverter.Type, IParameterConverter> parameterSetters;

  public ParameterConverterServiceImpl() {
    Map<IParameterConverter.Type, IParameterConverter> settersMap 
        = new HashMap<IParameterConverter.Type, IParameterConverter>();

    // TIME
    putSetters(settersMap, new ParameterConverter_TIME_utilDate());
    putSetters(settersMap, new ParameterConverter_TIME_Time());

    // TIMESTAMP
    putSetters(settersMap, new ParameterConverter_TIMESTAMP_utilDate());
    putSetters(settersMap, new ParameterConverter_TIMESTAMP_Timestamp());

    // DATE
    putSetters(settersMap, new ParameterConverter_DATE_utilDate());
    putSetters(settersMap, new ParameterConverter_DATE_sqlDate());

    // INTEGER
    putSetters(settersMap, new ParameterConverter_INTEGER_int());
    putSetters(settersMap, new ParameterConverter_INTEGER_langInteger());
    putSetters(settersMap, new ParameterConverter_INTEGER_long());
    putSetters(settersMap, new ParameterConverter_INTEGER_langLong());
    putSetters(settersMap, new ParameterConverter_INTEGER_boolean());

    // BIGINT
    putSetters(settersMap, new ParameterConverter_BIGINT_long());
    putSetters(settersMap, new ParameterConverter_BIGINT_langLong());
    putSetters(settersMap, new ParameterConverter_BIGINT_BigInteger());

    // CHAR/VARCHAR to boolean 
    putSetters(settersMap, new ParameterConverter_CHAR_boolean());
    putSetters(settersMap, new ParameterConverter_CHAR_langBoolean());
    putSetters(settersMap, new ParameterConverter_VARCHAR_boolean());
    putSetters(settersMap, new ParameterConverter_VARCHAR_langBoolean());

    // DECIMAL
    putSetters(settersMap, new ParameterConverter_DECIMAL_double());
    putSetters(settersMap, new ParameterConverter_DECIMAL_langDouble());
    putSetters(settersMap, new ParameterConverter_DECIMAL_BigDecimal());

    // NUMERIC
    putSetters(settersMap, new ParameterConverter_NUMERIC_double());
    putSetters(settersMap, new ParameterConverter_NUMERIC_langDouble());
    putSetters(settersMap, new ParameterConverter_NUMERIC_BigDecimal());

    // VARCHAR, CHAR and LONGVARCHAR
    putSetters(settersMap, new ParameterConverter_CHAR_String());
    putSetters(settersMap, new ParameterConverter_VARCHAR_String());
    putSetters(settersMap, new ParameterConverter_LONGVARCHAR_String());

    // byte[]
    putSetters(settersMap, new ParameterConverter_BINARY_byteArray());
    putSetters(settersMap, new ParameterConverter_VARBINARY_byteArray());
    putSetters(settersMap, new ParameterConverter_LONGVARBINARY_byteArray());

    // REAL
    putSetters(settersMap, new ParameterConverter_REAL_double());
    putSetters(settersMap, new ParameterConverter_REAL_langDouble());
    putSetters(settersMap, new ParameterConverter_REAL_BigDecimal());

    // DOUBLE
    putSetters(settersMap, new ParameterConverter_DOUBLE_double());
    putSetters(settersMap, new ParameterConverter_DOUBLE_langDouble());

    parameterSetters = Collections.unmodifiableMap(settersMap);
  }
  
  public IParameterConverter getConverter(int sqlType) {
    IParameterConverter converter = parameterSetters.get(new IParameterConverter.Type(sqlType));
    if (converter == null) {
      throw new IllegalArgumentException("No convert for java.sql.Types." 
          + TypeNameUtil.getName(sqlType));
    }
    return converter;
  }

  public IParameterConverter getConverter(int sqlType, Class javaType) {
    IParameterConverter converter 
        = parameterSetters.get(new IParameterConverter.Type(sqlType, javaType));
    if (converter == null) {
      throw new IllegalArgumentException("No convert for java.sql.Types." 
          + TypeNameUtil.getName(sqlType) + " and " + javaType);
    }
    return converter;
  }

  public IParameterConverter getConverter(IParameterConverter.Type type) {
    IParameterConverter converter = parameterSetters.get(type);
    if (converter == null) {
      throw new IllegalArgumentException("Converter type [" + type + "] is not supported.");
    }
    return converter;
  }

  private void putSetters(Map<IParameterConverter.Type, IParameterConverter> map, 
      IParameterConverter parameterConverter) {
    if (map.containsKey(parameterConverter.getType())) {
      throw new IllegalStateException("Converter " + parameterConverter + " is already registered");
    }
    map.put(parameterConverter.getType(), parameterConverter);
  }
  
  public String toString() {
    return "ParameterConverterServiceImpl[parameterSetters=" + parameterSetters + "]";
  }
}
