package com.googlecode.jdbcproc.daofactory.it.testdao.dao;

import com.googlecode.jdbcproc.daofactory.annotation.AStoredProcedure;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
@Transactional( propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED )
public interface ITxManagerDao {


    @AStoredProcedure(name = "tx_table_test")
    void runFailureProcedure();

    @AStoredProcedure(name = "tx_table_test_success")
    void runSuccessProcedure();


}
