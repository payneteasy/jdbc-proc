package com.googlecode.jdbcproc.daofactory.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Sets username and principal name in stored procedure
 */
@Retention(RUNTIME)
@Target(ElementType.METHOD)
public @interface AMetaLoginInfo {
}
