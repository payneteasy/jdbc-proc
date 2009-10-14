package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.dao.DataAccessException;

import junit.framework.TestCase;

/**
 * Tests for skip arguments methods
 */
public class AbstractParametersSetterBlockTest extends TestCase {
    
    public void testSkipCollectionArguments() {
        AbstractParametersSetterBlock setterBlock = new AbstractParametersSetterBlock() {
            public void setParameters(CallableStatement aStmt, Object[] aArgs) 
                throws DataAccessException, SQLException {
                throw new AssertionError();
            }
        };
        int arg1 = 5;
        List<String> arg2 = new ArrayList<String>();
        Long arg3 = (long) 5;
        List<Integer> arg4 = new ArrayList<Integer>();
        List<Integer> arg5 = new ArrayList<Integer>();
        List arg6 = new ArrayList();
        Date arg7 = new Date();
        Object[] args = new Object[] {arg1, arg2, arg3, arg4, arg5, arg6, arg7};
        
        Object[] result = setterBlock.skipCollectionArguments(args);
        assertNotNull(result);
        assertEquals(3, result.length);
        assertNotSame(arg2, result[1]);
        assertEquals(arg1, result[0]);
        assertEquals(arg3, result[1]);
        assertEquals(arg7, result[2]);
    }
    
    public void testSkipNonCollectionArguments() {
        AbstractParametersSetterBlock setterBlock = new AbstractParametersSetterBlock() {
            public void setParameters(CallableStatement aStmt, Object[] aArgs) 
                throws DataAccessException, SQLException {
                throw new AssertionError();
            }
        };
        int arg1 = 5;
        List<String> arg2 = new ArrayList<String>();
        Long arg3 = (long) 5;
        List<Integer> arg4 = new ArrayList<Integer>();
        List<Integer> arg5 = new ArrayList<Integer>();
        List arg6 = new ArrayList();
        Date arg7 = new Date();
        Object[] args = new Object[] {arg1, arg2, arg3, arg4, arg5, arg6, arg7};
        
        Object[] result = setterBlock.skipNonCollectionArguments(args);
        assertNotNull(result);
        assertEquals(4, result.length);
        assertNotSame(arg1, result[0]);
        assertEquals(arg2, result[0]);
        assertEquals(arg4, result[1]);
        assertEquals(arg5, result[2]);
        assertEquals(arg6, result[3]);
    }
}