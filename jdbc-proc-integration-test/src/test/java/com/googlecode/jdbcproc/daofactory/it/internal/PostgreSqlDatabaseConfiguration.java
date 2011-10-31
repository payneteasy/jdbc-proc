package com.googlecode.jdbcproc.daofactory.it.internal;

public class PostgreSqlDatabaseConfiguration implements IDatabaseConfiguration {

    public String getDataSourceSpringConfigLocation() {
        return "/spring/test-postgresql-datasource.xml";
    }

    public String getSpringSuffix() {
        return "postgresql";
    }

    public String getSqlDirectory() {
        return "sql_postgresql";
    }

    public String[] createEnvironment() {
        return new String[]{"PGPASSWORD=jdbcproc"};
    }

    public String[] createExecParameters(String aDatabaseName, String aFilePath) {
        return new String[]{
                  "psql"
                , "-U"
                , "jdbcproc"
                , aDatabaseName
                , "-f"
                , aFilePath
        };
    }
}
