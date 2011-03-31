package com.googlecode.jdbcproc.daofactory.it.testdao.service.impl;

import com.googlecode.jdbcproc.daofactory.it.testdao.dao.ITxManagerDao;
import com.googlecode.jdbcproc.daofactory.it.testdao.service.ITxManagerService;
import org.springframework.transaction.annotation.Transactional;

/**
 * tx manager test
 */
@Transactional
public class TxManagerServiceImpl implements ITxManagerService {

    public void runFailureProcedure() {

        theTxManagerDao.runSuccessProcedure();

        theTxManagerDao.runFailureProcedure();

    }


    /** tx manager dao */
    public void setTxManagerDao(ITxManagerDao aTxManagerDao) { theTxManagerDao = aTxManagerDao; }

    /** tx manager dao */
    private ITxManagerDao theTxManagerDao;
}
