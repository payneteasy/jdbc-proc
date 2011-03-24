/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.googlecode.jdbcproc.daofactory.guice;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.TestCase;

public abstract class AbstractDatabaseTest extends TestCase {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractDatabaseTest.class);

  @Override public void runBare() throws Throwable {
    // drops and creates database
    executeMysql("", "src/test/resources/sql/create_database.sql");

    // creates database schema
    executeMysql("jdbcprocdb", "src/test/resources/sql/resultset_info.sql");
    executeMysql("jdbcprocdb", "src/test/resources/sql/get_procedures_resultset.sql");

    executeMysql("jdbcprocdb", "src/test/resources/sql/company.sql");
    executeMysql("jdbcprocdb", "src/test/resources/sql/employee.sql");
    executeMysql("jdbcprocdb", "src/test/resources/sql/certificate.sql");
    executeMysql("jdbcprocdb", "src/test/resources/sql/ancestry.sql");
    executeMysql("jdbcprocdb", "src/test/resources/sql/verticality/carabiner.sql");
    executeMysql("jdbcprocdb", "src/test/resources/sql/verticality/dynamic_rope.sql");
    executeMysql("jdbcprocdb", "src/test/resources/sql/verticality/harness.sql");
    executeMysql("jdbcprocdb", "src/test/resources/sql/verticality/chalk_bag.sql");
    executeMysql("jdbcprocdb", "src/test/resources/sql/create_company.sql");
    executeMysql("jdbcprocdb", "src/test/resources/sql/create_company_secured.sql");
    executeMysql("jdbcprocdb", "src/test/resources/sql/create_employee.sql");
    executeMysql("jdbcprocdb", "src/test/resources/sql/get_employee_by_id.sql");
    executeMysql("jdbcprocdb", "src/test/resources/sql/get_company_employees.sql");
    executeMysql("jdbcprocdb", "src/test/resources/sql/get_companies.sql");
    executeMysql("jdbcprocdb", "src/test/resources/sql/get_companies_names.sql");
    executeMysql("jdbcprocdb", "src/test/resources/sql/get_companies_names_secured.sql");
    executeMysql("jdbcprocdb", "src/test/resources/sql/get_company_2x.sql");
    executeMysql("jdbcprocdb", "src/test/resources/sql/get_ancestry_2x_multi_level_grouping.sql");
    executeMysql("jdbcprocdb", "src/test/resources/sql/create_certificate.sql");

    executeMysql("jdbcprocdb", "src/test/resources/sql/verticality/create_collections.prc");
    executeMysql("jdbcprocdb", "src/test/resources/sql/verticality/upload_carabiners.prc");
    executeMysql("jdbcprocdb", "src/test/resources/sql/verticality/upload_harnesses.prc");
    executeMysql("jdbcprocdb", "src/test/resources/sql/verticality/upload_dynamic_ropes.prc");
    executeMysql("jdbcprocdb", "src/test/resources/sql/verticality/upload_verticality.prc");
    executeMysql("jdbcprocdb", "src/test/resources/sql/verticality/create_chalk_bag.prc");
    executeMysql("jdbcprocdb", "src/test/resources/sql/verticality/get_chalk_bags.prc");
    
    super.runBare();
  }

  private static void executeMysql(String aDatabase, String aSqlFile)
      throws IOException, InterruptedException {
    LOG.debug("Loading {}...", aSqlFile);
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
}
