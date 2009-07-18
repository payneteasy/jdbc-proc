package com.googlecode.jdbcproc.daofactory.it;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
public abstract class DatabaseAwareTest extends AbstractDependencyInjectionSpringContextTests {

    public void runBare() throws Throwable {
        // drops and creates database
        executeMysql("", "src/test/resources/sql/create_database.sql");

        // creates database schema
        executeMysql("jdbcprocdb", "src/test/resources/sql/company.sql");
        executeMysql("jdbcprocdb", "src/test/resources/sql/create_company.sql");
        executeMysql("jdbcprocdb", "src/test/resources/sql/get_procedures_resultset.sql");

        super.runBare();
    }

    protected void onSetUp() throws Exception {
    }

    private void executeMysql(String aDatabase, String aSqlFile) throws IOException, InterruptedException {
        final Process process = Runtime.getRuntime().exec(
                new String[]{
                        "mysql"
                        , "-u"
                        , "jdbcproc"
                        , "-pjdbcproc"
                        , aDatabase
                        , "-e"
                        , "source " + aSqlFile
                }
        );

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
                "/spring/test-datasource.xml"
                , "/spring/test-dao.xml"
        };
    }

    public void setDataSource(DataSource aDataSource) {
        theDataSource = aDataSource;
    }

    protected DataSource theDataSource;
    protected HibernateTemplate theHibernateTemplate;
}