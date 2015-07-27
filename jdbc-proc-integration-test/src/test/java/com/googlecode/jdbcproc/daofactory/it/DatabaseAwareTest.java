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
        executeSql("", "create_database.sql");

        // creates database schema
        executeSql("jdbcprocdb", "resultset_info.sql");
        executeSql("jdbcprocdb", "get_procedures_resultset.sql");
        
        executeSql("jdbcprocdb", "company.sql");
        executeSql("jdbcprocdb", "employee.sql");
        executeSql("jdbcprocdb", "certificate.sql");
        executeSql("jdbcprocdb", "ancestry.sql");
        executeSql("jdbcprocdb", "verticality/carabiner.sql");
        executeSql("jdbcprocdb", "verticality/dynamic_rope.sql");
        executeSql("jdbcprocdb", "verticality/harness.sql");
        executeSql("jdbcprocdb", "verticality/chalk_bag.sql");
        executeSql("jdbcprocdb", "create_company.sql");
        executeSql("jdbcprocdb", "create_company_secured.sql");
        executeSql("jdbcprocdb", "create_company_secured_consumer_key.sql");
        executeSql("jdbcprocdb", "create_employee.sql");
        executeSql("jdbcprocdb", "get_employee_by_id.sql");
        executeSql("jdbcprocdb", "get_company_employees.sql");
        executeSql("jdbcprocdb", "get_companies.sql");
        executeSql("jdbcprocdb", "get_companies_names.sql");
        executeSql("jdbcprocdb", "get_companies_names_secured.sql");
        executeSql("jdbcprocdb", "get_company_2x.sql");
        executeSql("jdbcprocdb", "get_ancestry_2x_multi_level_grouping.sql");
        executeSql("jdbcprocdb", "create_certificate.sql");
        executeSql("jdbcprocdb", "create_entity_with_list.sql");
        executeSql("jdbcprocdb", "create_entity_with_two_lists.sql");
        executeSql("jdbcprocdb", "create_entity_with_two_lists_and_metalogin_info.sql");
        executeSql("jdbcprocdb", "update_entity_with_list.sql");

        executeSql("jdbcprocdb", "verticality/create_collections.prc");
        executeSql("jdbcprocdb", "verticality/upload_carabiners.prc");
        executeSql("jdbcprocdb", "verticality/upload_carabiners_2.prc");
        executeSql("jdbcprocdb", "verticality/upload_carabiners_with_meta_login_info.prc");
        executeSql("jdbcprocdb", "verticality/upload_dynamic_ropes.prc");
        executeSql("jdbcprocdb", "verticality/upload_dynamic_ropes_with_meta_login_info.prc");
        executeSql("jdbcprocdb", "verticality/upload_verticality.prc");
        executeSql("jdbcprocdb", "verticality/upload_verticality_with_meta_login_info.prc");
        executeSql("jdbcprocdb", "verticality/upload_harnesses.prc");
        executeSql("jdbcprocdb", "verticality/create_chalk_bag.prc");
        executeSql("jdbcprocdb", "verticality/get_chalk_bags.prc");

          // tx manager
        executeSql("jdbcprocdb", "tx_table.sql");
        executeSql("jdbcprocdb", "tx_table_test.sql");
        executeSql("jdbcprocdb", "tx_table_test_success.sql");

        super.runBare();
    }

    protected void onSetUp() throws Exception {
    }

    
    private void executeSql(String aDatabase, String aSqlFilePath) throws IOException, InterruptedException {
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
