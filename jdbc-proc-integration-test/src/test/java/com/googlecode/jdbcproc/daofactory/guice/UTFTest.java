package com.googlecode.jdbcproc.daofactory.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.googlecode.jdbcproc.daofactory.DAOMethodInfo;
import com.googlecode.jdbcproc.daofactory.DaoMethodInfoFactory;
import com.googlecode.jdbcproc.daofactory.IMetaLoginInfoService;
import com.googlecode.jdbcproc.daofactory.guice.SimpleModule;
import com.googlecode.jdbcproc.daofactory.guice.StoredProcedureDaoProvider;
import com.googlecode.jdbcproc.daofactory.impl.block.service.ResultSetConverterBlockServiceImpl;
import com.googlecode.jdbcproc.daofactory.it.DatabaseAwareTest;
import com.googlecode.jdbcproc.daofactory.it.testdao.dao.ICompanyConsumerKeyDao;
import com.googlecode.jdbcproc.daofactory.it.testdao.dao.ICompanyDao;
import com.googlecode.jdbcproc.daofactory.it.testdao.dao.IEmployeeDao;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.Company;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.Employee;
import com.googlecode.jdbcproc.daofactory.it.testdao.service.impl.SimpleMetaLoginInfoServiceImpl;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

public class UTFTest extends DatabaseAwareTest {
    private Injector injector;
    private ICompanyDao theCompanyDao;
    private IEmployeeDao theEmployeeDao;

    @Override public void onSetUp() {
        injector = Guice.createInjector(new SimpleModule());

        theCompanyDao = injector.getInstance(ICompanyDao.class);
        theEmployeeDao = injector.getInstance(IEmployeeDao.class);
    }

    @Test
    public void test() {
        Company company = new Company();
        theCompanyDao.createCompany(company);

        Employee employee = new Employee();
        employee.setFirstname("First\uD83D\uDD8Cname");
        employee.setLastname("Lastname");
        employee.setCompany(company);
        theEmployeeDao.createEmployee(employee);

        Employee employeeFromBase = theEmployeeDao.getEmployeeById(employee.getId());
        assertNotNull(employeeFromBase);
        assertEquals("Firstname", employeeFromBase.getFirstname());
    }
}