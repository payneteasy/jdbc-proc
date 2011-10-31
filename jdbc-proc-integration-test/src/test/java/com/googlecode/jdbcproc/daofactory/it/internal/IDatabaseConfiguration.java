package com.googlecode.jdbcproc.daofactory.it.internal;

public interface IDatabaseConfiguration {

    String getSpringSuffix();

    String[] createExecParameters(String aDatabaseName, String aFilePath);

    String getSqlDirectory();

    String[] createEnvironment();

}
