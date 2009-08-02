package com.googlecode.jdbcproc.daofactory.impl.block.factory;

import com.googlecode.jdbcproc.daofactory.impl.block.ICallableStatementExecutorBlock;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.CallableStatementExecutorBlockExecute;
import com.googlecode.jdbcproc.daofactory.impl.procedureinfo.StoredProcedureInfo;

import java.lang.reflect.Method;

/**
 * Creates ICallableStatementExecutorBlock
 */
public class CallableStatementExecutorBlockFactory {

    public ICallableStatementExecutorBlock create(Method aDaoMethod, StoredProcedureInfo aProcedureInfo) {
        return new CallableStatementExecutorBlockExecute();
    }
}
