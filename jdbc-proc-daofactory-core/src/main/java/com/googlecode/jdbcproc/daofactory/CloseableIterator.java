package com.googlecode.jdbcproc.daofactory;

import java.util.Iterator;

/**
 * Iterator with close method to release resources 
 */
public interface CloseableIterator<T> extends Iterator<T>, AutoCloseable {

    /**
     * Close resources
     */
    void close();
}
