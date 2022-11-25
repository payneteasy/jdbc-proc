package com.googlecode.jdbcproc.daofactory.it.internal;

public class MysqlDatabaseConfiguration implements IDatabaseConfiguration {
    public String getDataSourceSpringConfigLocation() {
        return "/spring/test-mysql-datasource.xml";
    }

    public String getSpringSuffix() {
        return "mysql";
    }

    public String getSqlDirectory() {
        return "sql_mysql";
    }

    public String[] createEnvironment() {
        return null;
    }

    public String[] createExecParameters(String aDatabaseName, String aFilePath) {
        return new String[]{
                "mysql"
                , "-u"
                , "jdbcproc"
                , "-pjdbcproc"
                , aDatabaseName
                , "-h", "127.0.0.1"
                , "-e"
                , "source " + aFilePath
        };
    }
}
