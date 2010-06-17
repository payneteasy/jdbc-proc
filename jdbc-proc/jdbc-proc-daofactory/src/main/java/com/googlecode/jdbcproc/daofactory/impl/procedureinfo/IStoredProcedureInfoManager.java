package com.googlecode.jdbcproc.daofactory.impl.procedureinfo;

/**
 * procedure info manager
 */
public interface IStoredProcedureInfoManager {
    StoredProcedureInfo getProcedureInfo(String aProcedureName);
}
