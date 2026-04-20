package com.googlecode.jdbcproc.daofactory.it.testdao.dao;

import com.googlecode.jdbcproc.daofactory.annotation.AStoredProcedure;

public interface ISleepDao {

    @AStoredProcedure(name = "sleep_proc", callTimeoutSeconds = 1)
    void sleepWithTimeout(int seconds);

    @AStoredProcedure(name = "sleep_proc")
    void sleepNoTimeout(int seconds);
}
