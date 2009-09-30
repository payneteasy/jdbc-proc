package ${packageName};

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import java.util.List;

/**
 *
 */
public class CompanyDaoTest extends AbstractDependencyInjectionSpringContextTests {

    public void testCreateCompany() {
        Company company = new Company();
        company.setName("first");
        theCompanyDao.createCompany(company);
        assertNotNull(company.getId());
    }
    
    /** Company dao */
    public void setCompanyDao(ICompanyDao aCompanyDao) {
        theCompanyDao = aCompanyDao;
    }

    protected String[] getConfigLocations() {
        return new String[]{
                "/spring/spring-datasource.xml"
                , "/spring/spring-dao.xml"
        };
    }

    /**
     * Company dao
     */
    private ICompanyDao theCompanyDao;

}
