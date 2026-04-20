package com.googlecode.jdbcproc.daofactory.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Stored procedure
 */
@Retention(RUNTIME)
@Target(ElementType.METHOD)
public @interface AStoredProcedure {
    /**
     * Stored procedure name
     * @return name of store procedure
     */
    String name();

    /**
     * Query timeout for the stored procedure call, in seconds.
     * Zero (default) means no timeout. Applied via {@link java.sql.Statement#setQueryTimeout(int)}.
     * @return timeout in seconds, or 0 for none
     */
    int callTimeoutSeconds() default 0;
}
