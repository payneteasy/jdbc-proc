package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.block.IRegisterOutParametersBlock;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * Register output parameters
 */
public class RegisterOutParametersBlockImpl implements IRegisterOutParametersBlock {

    public RegisterOutParametersBlockImpl(List<IndexDataTypePair> aList) {
        theList = Collections.unmodifiableList(aList);
    }

    public void registerOutParameters(CallableStatement aStmt) throws SQLException {
        for (IndexDataTypePair pair : theList) {
            aStmt.registerOutParameter(pair.getIndex(), pair.getDataType());
        }
    }

    private List<IndexDataTypePair> theList ;
}
