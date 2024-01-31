package com.googlecode.jdbcproc.daofactory.impl.block.service;

import com.googlecode.jdbcproc.daofactory.impl.parameterconverter.IParameterConverter;
import com.googlecode.jdbcproc.daofactory.impl.parameterconverter.ParameterConverterService;
import com.googlecode.jdbcproc.daofactory.impl.parameterconverter.IParameterConverter.Type;

final class NullParameterConverterService implements
        ParameterConverterService {
    public IParameterConverter getConverter(Type type) {
        return null;
    }

    public IParameterConverter getConverter(int sqlType, Class javaType) {
        return null;
    }

    public IParameterConverter getConverter(int sqlType) {
        return null;
    }
}