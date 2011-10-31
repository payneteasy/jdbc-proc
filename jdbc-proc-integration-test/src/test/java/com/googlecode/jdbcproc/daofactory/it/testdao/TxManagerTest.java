package com.googlecode.jdbcproc.daofactory.it.testdao;

import com.googlecode.jdbcproc.daofactory.it.DatabaseAwareTest;
import com.googlecode.jdbcproc.daofactory.it.testdao.service.ITxManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * tx manager
 */
public class TxManagerTest extends DatabaseAwareTest {

    private static final Logger LOG = LoggerFactory.getLogger(TxManagerTest.class);

    public void test() {

        try {
            theTxManagerService.runFailureProcedure();
        } catch (Exception e) {
            LOG.error("Expected exception:", e);
        }


    }

    @Override protected String[] getConfigLocations() {
        return new String[] {"/spring/test-mysql-datasource.xml"
          , "/spring/test-dao.xml"
        };
    }

    /** ITxManagerService */
    public void setTxManagerService(ITxManagerService aTxManagerService) { theTxManagerService = aTxManagerService; }

    /** ITxManagerService */
    private ITxManagerService theTxManagerService;
}
