package ${packageName};

import com.googlecode.jdbcproc.daofactory.annotation.AStoredProcedure;

import java.util.List;
import java.util.Iterator;

/**
 * Test dao
 */
public interface ICompanyDao {

    /**
     * Creates company
     *
     * @param aCompany new company
     */
    @AStoredProcedure(name = "create_company")
    void createCompany(Company aCompany);

}
