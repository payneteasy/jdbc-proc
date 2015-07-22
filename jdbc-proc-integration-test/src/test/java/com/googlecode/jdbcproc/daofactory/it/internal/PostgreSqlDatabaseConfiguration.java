package com.googlecode.jdbcproc.daofactory.it.internal;

import org.springframework.util.StringUtils;

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
        if (StringUtils.hasText(aDatabaseName)) {
            return new String[]{
                    "psql"
                    , "-U"
                    , "jdbcproc"
                    , aDatabaseName
                    , "-f"
                    , aFilePath
            };
        } else {
            return new String[]{
                    "psql"
                    , "-f"
                    , aFilePath
            };
        }
    }
}
