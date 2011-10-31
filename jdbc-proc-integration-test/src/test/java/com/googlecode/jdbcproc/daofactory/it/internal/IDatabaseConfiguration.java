package com.googlecode.jdbcproc.daofactory.it.internal;

public interface IDatabaseConfiguration {

    String getDataSourceSpringConfigLocation();

    String[] createExecParameters(String aDatabaseName, String aFilePath);
}
