package com.googlecode.jdbcproc.daofactory.impl.block.service;

import java.lang.reflect.Method;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.googlecode.jdbcproc.daofactory.IMetaLoginInfoService;
import com.googlecode.jdbcproc.daofactory.ITestDao;
import com.googlecode.jdbcproc.daofactory.impl.block.IParametersSetterBlock;
import com.googlecode.jdbcproc.daofactory.impl.parameterconverter.IParameterConverter;
import com.googlecode.jdbcproc.daofactory.impl.parameterconverter.ParameterConverterService;
import com.googlecode.jdbcproc.daofactory.impl.parameterconverter.IParameterConverter.Type;
import com.googlecode.jdbcproc.daofactory.impl.procedureinfo.StoredProcedureArgumentInfo;
import com.googlecode.jdbcproc.daofactory.impl.procedureinfo.StoredProcedureInfo;

import junit.framework.TestCase;

/**
 * TODO: this test is a quick-fix. It's better to refactor
 * ParametersSetterBlockServiceImpl creation logic to make it more testable.
 */
public class ParametersSetterBlockServiceImplTest extends TestCase {
    @SuppressWarnings("rawtypes")
    private ParameterConverterService nullConverterService = new ParameterConverterService() {
        public IParameterConverter getConverter(Type type) {
            return null;
        }
        
        public IParameterConverter getConverter(int sqlType, Class javaType) {
            return null;
        }
        
        public IParameterConverter getConverter(int sqlType) {
            return null;
        }
    };
    private IMetaLoginInfoService metaLoginInfoService = new IMetaLoginInfoService() {
        
        public String getUsernameParameterName() {
            return "i_username";
        }
        
        public String getUsername() {
            return "admin";
        }
        
        public String getRoleParameterName() {
            return "i_role";
        }
        
        public String getRole() {
            return "admin";
        }
    };
    
    public void testMethodWithOneListArg() throws SecurityException, NoSuchMethodException {
        final List<IParametersSetterBlock> saveMethodMarker = new ArrayList<IParametersSetterBlock>();
        final List<IParametersSetterBlock> createAllListMarker = new ArrayList<IParametersSetterBlock>();
        ParametersSetterBlockServiceImpl setter = new ParametersSetterBlockServiceImpl() {
            @Override
            protected List<IParametersSetterBlock> createSaveMethod(
                    ParameterConverterService converterService, Method method,
                    StoredProcedureInfo procedureInfo) {
                return saveMethodMarker;
            }

            @Override
            protected List<IParametersSetterBlock> createAllList(
                    JdbcTemplate jdbcTemplate,
                    ParameterConverterService converterService, Method method) {
                return createAllListMarker;
            }
        };
        StoredProcedureInfo procedureInfo;
        List<IParametersSetterBlock> blocks;
        
        procedureInfo = new StoredProcedureInfo("save_list_not_entity");
        blocks = setter.create(null, nullConverterService,
                ITestDao.class.getMethod("saveListNotEntity", new Class[]{List.class}),
                procedureInfo, null);
        
        assertSame(createAllListMarker, blocks);
        
        procedureInfo = new StoredProcedureInfo("save_list_not_entity");
        procedureInfo.addColumn(new StoredProcedureArgumentInfo("i_a", (short) StoredProcedureArgumentInfo.IN, (short) Types.INTEGER));
        procedureInfo.addColumn(new StoredProcedureArgumentInfo("i_b", (short) StoredProcedureArgumentInfo.IN, (short) Types.VARCHAR));
        try {
            blocks = setter.create(null, nullConverterService,
                    ITestDao.class.getMethod("saveListNotEntity", new Class[]{List.class}),
                    procedureInfo, null);
            fail("Exception is expected here");
        } catch (IllegalStateException e) {
            //expected
        }
    }

    /**
     * @see http://code.google.com/p/jdbc-proc/issues/detail?id=15
     */
    public void testSingleListParameterAndMetaLoginInfo() throws SecurityException, NoSuchMethodException {
        final List<IParametersSetterBlock> createListAndArgumentsWithMetaLoginInfo = new ArrayList<IParametersSetterBlock>();
        ParametersSetterBlockServiceImpl setter = new ParametersSetterBlockServiceImpl() {
            @Override
            protected List<IParametersSetterBlock> createListAndArgumentsWithMetaLoginInfo(
                    JdbcTemplate jdbcTemplate,
                    ParameterConverterService converterService, Method method,
                    StoredProcedureInfo procedureInfo,
                    IMetaLoginInfoService aMetaLoginInfoService) {
                return createListAndArgumentsWithMetaLoginInfo;
            }
        };
        StoredProcedureInfo procedureInfo;
        List<IParametersSetterBlock> blocks;
        
        procedureInfo = new StoredProcedureInfo("call_with_list");
        procedureInfo.addColumn(new StoredProcedureArgumentInfo("i_username", (short) StoredProcedureArgumentInfo.IN, (short) Types.VARCHAR));
        procedureInfo.addColumn(new StoredProcedureArgumentInfo("i_role", (short) StoredProcedureArgumentInfo.IN, (short) Types.VARCHAR));
        blocks = setter.create(null, nullConverterService,
                ITestDao.class.getMethod("callWithList", new Class[]{List.class}),
                procedureInfo, metaLoginInfoService);
        
        assertSame(createListAndArgumentsWithMetaLoginInfo, blocks);
    }
}