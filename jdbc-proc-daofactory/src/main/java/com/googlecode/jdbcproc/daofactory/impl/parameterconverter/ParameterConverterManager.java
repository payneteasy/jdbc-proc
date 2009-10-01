package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import com.googlecode.jdbcproc.daofactory.impl.TypeNameUtil;

import java.util.HashMap;
import java.util.Collections;
import java.util.Map;

/**
 * 
 */
public class ParameterConverterManager {

    public ParameterConverterManager() {
        HashMap<ParameterSetterKey, IParameterConverter> settersMap = new HashMap<ParameterSetterKey, IParameterConverter>();

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

        // CHAR/VARCHAR to boolean 
        putSetters(settersMap, new ParameterConverter_CHAR_boolean());
        putSetters(settersMap, new ParameterConverter_CHAR_langBoolean());
        putSetters(settersMap, new ParameterConverter_VARCHAR_boolean());
        putSetters(settersMap, new ParameterConverter_VARCHAR_langBoolean());

        // DECIMAL
        putSetters(settersMap, new ParameterConverter_DECIMAL_double());
        putSetters(settersMap, new ParameterConverter_DECIMAL_langDouble());
        putSetters(settersMap, new ParameterConverter_DECIMAL_BigDecimal());

        // VARCHAR, CHAR and LONGVARCHAR
        putSetters(settersMap, new ParameterConverter_CHAR_String());
        putSetters(settersMap, new ParameterConverter_VARCHAR_String());
        putSetters(settersMap, new ParameterConverter_LONGVARCHAR_String());

        // byte[]
        putSetters(settersMap, new ParameterConverter_LONGVARBINARY_byteArray());

        // REAL
        putSetters(settersMap, new ParameterConverter_REAL_double());
        putSetters(settersMap, new ParameterConverter_REAL_BigDecimal());

        theParameterSettersMap = Collections.unmodifiableMap(settersMap);
    }

    private void putSetters(HashMap<ParameterSetterKey, IParameterConverter> aMap, IParameterConverter aParameterConverter) {
        if(aMap.containsKey(aParameterConverter.getKey())) {
            throw new IllegalStateException("Converter "+aParameterConverter+" is already registered");
        }
        aMap.put(aParameterConverter.getKey(), aParameterConverter);
    }

    public IParameterConverter findConverter(int aColumnType, Class aType) {
        IParameterConverter paramConverter = theParameterSettersMap.get( new ParameterSetterKey(aColumnType, aType));
        if(paramConverter ==null) {
            throw new IllegalStateException("No convert for java.sql.Types."+ TypeNameUtil.getName(aColumnType)+ " and "+aType);
        }
        return paramConverter;
    }

    public String toString() {
        return "ParameterConverterManager{" +
                "theParameterSettersMap=" + theParameterSettersMap +
                '}';
    }

    private final Map<ParameterSetterKey, IParameterConverter> theParameterSettersMap;

}
