package com.googlecode.jdbcproc.daofactory.impl.block.factory;

import com.googlecode.jdbcproc.daofactory.impl.block.ICallableStatementExecutorBlock;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.CallableStatementExecutorBlockUpdateEquals1;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.CallableStatementExecutorBlockExecute;
import com.googlecode.jdbcproc.daofactory.impl.procedureinfo.StoredProcedureInfo;

import java.lang.reflect.Method;

/**
 * Creates ICallableStatementExecutorBlock
 */
public class CallableStatementExecutorBlockFactory {

    public ICallableStatementExecutorBlock create(Method aDaoMethod, StoredProcedureInfo aProcedureInfo) {
        // entity
        if(BlockFactoryUtils.isGetAllMethod(aDaoMethod, aProcedureInfo)) {
            return new CallableStatementExecutorBlockExecute();

        // first id and second is entity
        } else if(aProcedureInfo.getArgumentsCounts() > aDaoMethod.getParameterTypes().length) {
            return new CallableStatementExecutorBlockUpdateEquals1();

        // return from result set
        } else if( ! aDaoMethod.getReturnType().equals(void.class) ) {
            return new CallableStatementExecutorBlockExecute();

        // no arguments and no returns
        } else if ( aDaoMethod.getReturnType().equals(void.class) && aProcedureInfo.getArgumentsCounts()==0) {
            return new CallableStatementExecutorBlockExecute();

        // remove by id
        } else if ( aDaoMethod.getReturnType().equals(void.class) && aProcedureInfo.getArgumentsCounts()==1) {
            return new CallableStatementExecutorBlockExecute();
        } else {
            throw new IllegalStateException("Unsupported: "+aDaoMethod);
        }
    }
}
