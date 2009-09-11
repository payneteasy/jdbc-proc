package com.googlecode.jdbcproc.daofactory;

import java.util.Iterator;

/**
 * Iterator with close method to release resources 
 */
public interface CloseableIterator<T> extends Iterator<T> {

    /**
     * Close resources
     */
    void close();
}
