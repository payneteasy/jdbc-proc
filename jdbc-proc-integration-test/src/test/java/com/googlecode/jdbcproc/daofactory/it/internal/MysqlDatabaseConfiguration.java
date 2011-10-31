package com.googlecode.jdbcproc.daofactory.it.internal;

public class MysqlDatabaseConfiguration implements IDatabaseConfiguration {
    public String getDataSourceSpringConfigLocation() {
        return "/spring/test-mysql-datasource.xml";
    }

    public String[] createExecParameters(String aDatabaseName, String aFilePath) {
        return new String[]{
                "mysql"
                , "-u"
                , "jdbcproc"
                , "-pjdbcproc"
                , aDatabaseName
                , "-e"
                , "source " + aFilePath
        };
    }
}
