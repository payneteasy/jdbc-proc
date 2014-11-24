package com.googlecode.jdbcproc.daofactory.it;

import com.googlecode.jdbcproc.daofactory.it.internal.IDatabaseConfiguration;
import com.googlecode.jdbcproc.daofactory.it.internal.MysqlDatabaseConfiguration;
import com.googlecode.jdbcproc.daofactory.it.internal.PostgreSqlDatabaseConfiguration;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
public abstract class DatabaseAwareTest extends AbstractDependencyInjectionSpringContextTests {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    public DatabaseAwareTest() {

        String dialect = System.getProperty("dialect", "mysql");
        if(dialect.equals("mysql")) {
            theDatabaseConfiguration = new MysqlDatabaseConfiguration();
        } else if (dialect.equals("postgresql")) {
            theDatabaseConfiguration = new PostgreSqlDatabaseConfiguration();
        } else {
            throw new IllegalStateException("dialect " + dialect + " not supported for test");
        }

    }

    public void runBare() throws Throwable {
        // drops and creates database
        executeMysql("", "create_database.sql");

        // creates database schema
        executeMysql("jdbcprocdb", "resultset_info.sql");
        executeMysql("jdbcprocdb", "get_procedures_resultset.sql");
        
        executeMysql("jdbcprocdb", "company.sql");
        executeMysql("jdbcprocdb", "employee.sql");
        executeMysql("jdbcprocdb", "certificate.sql");
        executeMysql("jdbcprocdb", "ancestry.sql");
        executeMysql("jdbcprocdb", "verticality/carabiner.sql");
        executeMysql("jdbcprocdb", "verticality/dynamic_rope.sql");
        executeMysql("jdbcprocdb", "verticality/harness.sql");
        executeMysql("jdbcprocdb", "verticality/chalk_bag.sql");
        executeMysql("jdbcprocdb", "create_company.sql");
        executeMysql("jdbcprocdb", "create_company_secured.sql");
        executeMysql("jdbcprocdb", "create_company_secured_consumer_key.sql");
        executeMysql("jdbcprocdb", "create_employee.sql");
        executeMysql("jdbcprocdb", "get_employee_by_id.sql");
        executeMysql("jdbcprocdb", "get_company_employees.sql");
        executeMysql("jdbcprocdb", "get_companies.sql");
        executeMysql("jdbcprocdb", "get_companies_names.sql");
        executeMysql("jdbcprocdb", "get_companies_names_secured.sql");
        executeMysql("jdbcprocdb", "get_company_2x.sql");
        executeMysql("jdbcprocdb", "get_ancestry_2x_multi_level_grouping.sql");
        executeMysql("jdbcprocdb", "create_certificate.sql");
        executeMysql("jdbcprocdb", "create_entity_with_list.sql");
        executeMysql("jdbcprocdb", "update_entity_with_list.sql");

        executeMysql("jdbcprocdb", "verticality/create_collections.prc");
        executeMysql("jdbcprocdb", "verticality/upload_carabiners.prc");
        executeMysql("jdbcprocdb", "verticality/upload_carabiners_2.prc");
        executeMysql("jdbcprocdb", "verticality/upload_carabiners_with_meta_login_info.prc");
        executeMysql("jdbcprocdb", "verticality/upload_dynamic_ropes.prc");
        executeMysql("jdbcprocdb", "verticality/upload_dynamic_ropes_with_meta_login_info.prc");
        executeMysql("jdbcprocdb", "verticality/upload_verticality.prc");
        executeMysql("jdbcprocdb", "verticality/upload_verticality_with_meta_login_info.prc");
        executeMysql("jdbcprocdb", "verticality/upload_harnesses.prc");
        executeMysql("jdbcprocdb", "verticality/create_chalk_bag.prc");
        executeMysql("jdbcprocdb", "verticality/get_chalk_bags.prc");

          // tx manager
          executeMysql("jdbcprocdb", "tx_table.sql");
          executeMysql("jdbcprocdb", "tx_table_test.sql");
          executeMysql("jdbcprocdb", "tx_table_test_success.sql");

        super.runBare();
    }

    protected void onSetUp() throws Exception {
    }

    
    private void executeMysql(String aDatabase, String aSqlFilePath) throws IOException, InterruptedException {
        String sqlFile = "src/test/resources/"+theDatabaseConfiguration.getSqlDirectory()+"/" + aSqlFilePath;

        LOG.debug("Loading {}...", sqlFile);
        final Process process = Runtime.getRuntime().exec( theDatabaseConfiguration.createExecParameters(aDatabase, sqlFile), theDatabaseConfiguration.createEnvironment());

        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    InputStream in = process.getInputStream();
                    try {
                        byte[] buf = new byte[1024];
                        int i;
                        while ((i = in.read(buf, 0, 1024)) > 0) {
                            System.out.println("STD: " + new String(buf, 0, i));
                        }

                    } finally {
                        in.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();

        Thread t2 = new Thread(new Runnable() {
            public void run() {
                try {
                    InputStream in = process.getErrorStream();
                    try {
                        byte[] buf = new byte[1024];
                        int i;
                        while ((i = in.read(buf, 0, 1024)) > 0) {
                            System.out.println("STD: " + new String(buf, 0, i));
                        }

                    } finally {
                        in.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t2.start();
        int result = process.waitFor();
        if (result != 0) {
            throw new IllegalStateException("Error while creating database: " + result);
        }
    }

    protected String[] getConfigLocations() {
        return new String[]{
                  getSpringConfig("datasource.xml")
                , getSpringConfig("factory-metalogin.xml")
                , "/spring/test-dao-metalogin.xml"
        };
    }

    protected String getSpringConfig(String aXmlName) {
        return "/spring/test-"+theDatabaseConfiguration.getSpringSuffix()+"-"+aXmlName;
    }

    public void setDataSource(DataSource aDataSource) {
        theDataSource = aDataSource;
    }

    protected final IDatabaseConfiguration theDatabaseConfiguration;
    protected DataSource theDataSource;
}
