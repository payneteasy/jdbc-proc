package com.googlecode.jdbcproc.daofactory.impl.block.factory;

import com.googlecode.jdbcproc.daofactory.impl.procedureinfo.StoredProcedureArgumentInfo;
import com.googlecode.jdbcproc.daofactory.impl.procedureinfo.StoredProcedureInfo;
import com.googlecode.jdbcproc.daofactory.impl.block.IRegisterOutParametersBlock;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.IndexDataTypePair;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.RegisterOutParametersBlockImpl;

import java.util.LinkedList;

/**
 * Creates IRegisterOutParametersBlock
 */
public class RegisterOutParametersBlockFactory {

    /**
     * Create IRegisterOutParametersBlock
     * @param aProcedureInfo procedure info
     * @return block
     */
    public IRegisterOutParametersBlock create(StoredProcedureInfo aProcedureInfo) {
        LinkedList<IndexDataTypePair> list = new LinkedList<IndexDataTypePair>();
        int index = 1 ;
        for (StoredProcedureArgumentInfo argumentInfo : aProcedureInfo.getArguments()) {
            if(argumentInfo.getColumnType() == StoredProcedureArgumentInfo.OUT ) {
                list.add(new IndexDataTypePair(index, argumentInfo.getColumnType()));
            }
            index++;
        }
        return list.size() > 0 ? new RegisterOutParametersBlockImpl(list) : null;
    }

}
