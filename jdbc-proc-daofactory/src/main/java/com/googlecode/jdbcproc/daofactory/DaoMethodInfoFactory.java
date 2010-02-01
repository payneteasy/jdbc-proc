package com.googlecode.jdbcproc.daofactory;

import com.googlecode.jdbcproc.daofactory.annotation.AStoredProcedure;
import com.googlecode.jdbcproc.daofactory.impl.DaoMethodInvoker;
import com.googlecode.jdbcproc.daofactory.impl.procedureinfo.StoredProcedureInfo;
import com.googlecode.jdbcproc.daofactory.impl.procedureinfo.StoredProcedureInfoManagerInitOnStartup;
import com.googlecode.jdbcproc.daofactory.impl.procedureinfo.IStoredProcedureInfoManager;
import com.googlecode.jdbcproc.daofactory.impl.block.factory.*;
import com.googlecode.jdbcproc.daofactory.impl.parameterconverter.ParameterConverterManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

import java.lang.reflect.Method;

/**
 * Creates DaoMethodInfo
 */
public class DaoMethodInfoFactory implements InitializingBean, DAOMethodInfo {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    /**
     * Creates method info for bets performance
     * @param aDaoMethod method
     * @return method info
     */
    public DaoMethodInvoker createDaoMethodInvoker(Method aDaoMethod) {
        AStoredProcedure procedureAnnotation = aDaoMethod.getAnnotation(AStoredProcedure.class);
        Assert.notNull(procedureAnnotation, "Method must have @AStoredProcedure annotation");

        String procedureName = procedureAnnotation.name();
        Assert.hasText(procedureName, "Method " + aDaoMethod.toString() + " has empty name() parameter in @AStoredProcedure annotation");

        StoredProcedureInfo procedureInfo = theStoredProcedureInfoManager.getProcedureInfo(procedureName);
        if(LOG.isDebugEnabled()) {
            LOG.debug("      Found procedure info: "+procedureInfo);
        }
        Assert.notNull(procedureInfo, "There are no procedure '" + procedureName + "' in database");

        String callString = createCallString(procedureInfo);

        boolean isReturnIterator = BlockFactoryUtils.isReturnIterator(aDaoMethod);

        return new DaoMethodInvoker(
                  procedureInfo.getProcedureName()
                , callString
                , theRegisterOutParametersBlockFactory    .create(procedureInfo)
                , theParametersSetterBlockFactory         .create(theJdbcTemplate, theParameterConverterManager, aDaoMethod, procedureInfo)
                , theCallableStatementExecutorBlockFactory.create(aDaoMethod, procedureInfo)
                , theOutputParametersGetterBlockFactory   .create(theParameterConverterManager, aDaoMethod, procedureInfo)
                , theResultSetConverterBlockFactory       .create(aDaoMethod, procedureInfo, theParameterConverterManager)
                , isReturnIterator
        );
    }


    /**
     * Create call query string.
     * <p/>
     * For example:
     * <code>
     * { call create_processors( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? ) }
     * </code>
     *
     * @param procedureInfo procedure info
     * @return call string
     */
    private String createCallString(StoredProcedureInfo procedureInfo) {
        StringBuilder sb = new StringBuilder();
        sb.append("{ call ");
        sb.append(procedureInfo.getProcedureName());
        sb.append("(");
        int j = 0;
        for (int i = 0; i < procedureInfo.getArgumentsCounts(); i++) {
            if (!procedureInfo.getArguments().get(i).isFunctionReturnParameter()) {
                if (j > 0) {
                    sb.append(", ? ");
                } else {
                    sb.append(" ? ");
                }
                j++;
            }
        }
        sb.append(") }");
        return sb.toString();
    }

    /**
     * Gets information about all procedures from database
     *
     * @throws Exception on error
     */
    public void afterPropertiesSet() throws Exception {
        if(theStoredProcedureInfoManager==null) {
            theStoredProcedureInfoManager = new StoredProcedureInfoManagerInitOnStartup(theJdbcTemplate);
        }
    }

    /**
     * JdbcTemplate
     * @param aJdbcTemplate jdbc template
     */
    public void setJdbcTemplate(JdbcTemplate aJdbcTemplate) {
        theJdbcTemplate = aJdbcTemplate;
    }

    /**
     * StoredProcedureInfoManager. If none using StoredProcedureInfoManagerInitOnStartup
     * 
     * @param aStoredProcedureInfoManager StoredProcedureInfoManager
     */
    public void setStoredProcedureInfoManager(IStoredProcedureInfoManager aStoredProcedureInfoManager) {
        theStoredProcedureInfoManager = aStoredProcedureInfoManager;
    }

    public String toString() {
        return "DaoMethodInfoFactory{" +
                "theJdbcTemplate=" + theJdbcTemplate +
                ", theParameterConverterManager=" + theParameterConverterManager +
                ", theStoredProcedureInfoManager=" + theStoredProcedureInfoManager +
                ", theCallableStatementExecutorBlockFactory=" + theCallableStatementExecutorBlockFactory +
                ", theOutputParametersGetterBlockFactory=" + theOutputParametersGetterBlockFactory +
                ", theParametersSetterBlockFactory=" + theParametersSetterBlockFactory +
                ", theRegisterOutParametersBlockFactory=" + theRegisterOutParametersBlockFactory +
                ", theResultSetConverterBlockFactory=" + theResultSetConverterBlockFactory +
                '}';
    }

    /**
     * JdbcTemplate
     */
    private JdbcTemplate theJdbcTemplate;

    private final ParameterConverterManager theParameterConverterManager = new ParameterConverterManager();
    private IStoredProcedureInfoManager theStoredProcedureInfoManager ;
    // factories
    private final CallableStatementExecutorBlockFactory theCallableStatementExecutorBlockFactory = new CallableStatementExecutorBlockFactory();
    private final OutputParametersGetterBlockFactory theOutputParametersGetterBlockFactory = new OutputParametersGetterBlockFactory();
    private final ParametersSetterBlockFactory theParametersSetterBlockFactory = new ParametersSetterBlockFactory();
    private final RegisterOutParametersBlockFactory theRegisterOutParametersBlockFactory = new RegisterOutParametersBlockFactory();
    private final ResultSetConverterBlockFactory theResultSetConverterBlockFactory = new ResultSetConverterBlockFactory();
}
