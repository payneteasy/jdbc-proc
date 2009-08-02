package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import java.util.HashMap;
import java.util.Collections;
import java.util.Map;

/**
 * 
 */
public class ParameterConverterManager {

    private static final ParameterConverter_ANY_Any PARAMETER_SETTER_ANY_ANY = new ParameterConverter_ANY_Any();

    public ParameterConverterManager() {
        HashMap<ParameterSetterKey, IParameterConverter> settersMap = new HashMap<ParameterSetterKey, IParameterConverter>();
        putSetters(settersMap, new ParameterConverter_TIMESTAMP_utilDate());
        putSetters(settersMap, new ParameterConverter_INTEGER_int());
        putSetters(settersMap, new ParameterConverter_INTEGER_long());
        putSetters(settersMap, new ParameterConverter_INTEGER_boolean());

        // boolean
        putSetters(settersMap, new ParameterConverter_CHAR_boolean());
        putSetters(settersMap, new ParameterConverter_CHAR_langBoolean());
        putSetters(settersMap, new ParameterConverter_VARCHAR_boolean());
        putSetters(settersMap, new ParameterConverter_VARCHAR_langBoolean());

        putSetters(settersMap, new ParameterConverter_DECIMAL_double());
        putSetters(settersMap, new ParameterConverter_DECIMAL_langDouble());

        theParameterSettersMap = Collections.unmodifiableMap(settersMap);
    }

    private void putSetters(HashMap<ParameterSetterKey, IParameterConverter> aMap, IParameterConverter aParameterConverter) {
        aMap.put(aParameterConverter.getKey(), aParameterConverter);
    }

    public IParameterConverter findConverter(int aColumnType, Class aType) {
        IParameterConverter paramConverter = theParameterSettersMap.get( new ParameterSetterKey(aColumnType, aType));
        if(paramConverter ==null) {
            paramConverter = PARAMETER_SETTER_ANY_ANY;
        }
        return paramConverter;
    }
    
    private final Map<ParameterSetterKey, IParameterConverter> theParameterSettersMap;

}
