package com.googlecode.jdbcproc.daofactory.impl.block.service;

import java.sql.Types;

import junit.framework.TestCase;

import com.googlecode.jdbcproc.daofactory.impl.block.IResultSetConverterBlock;
import com.googlecode.jdbcproc.daofactory.impl.parameterconverter.ParameterConverterServiceImpl;
import com.googlecode.jdbcproc.daofactory.impl.procedureinfo.ResultSetColumnInfo;
import com.googlecode.jdbcproc.daofactory.impl.procedureinfo.StoredProcedureInfo;
import com.mockobjects.sql.MockSingleRowResultSet;

public class ResultSetConverterBlockServiceImplTest extends TestCase {
    private ResultSetConverterBlockServiceImpl service;
    
    public void setUp() {
        service = new ResultSetConverterBlockServiceImpl();
    }
    
    public void testByteArrayReturnMapping() throws Exception {
        StoredProcedureInfo procedureInfo = new StoredProcedureInfo("get_blob");
        procedureInfo.addResultSetColumn(new ResultSetColumnInfo("content", Types.LONGVARBINARY));
        IResultSetConverterBlock converterBlock = service.create(IMappingTestDao.class.getMethod("returnsByteArray", new Class<?>[]{}),
                procedureInfo, new ParameterConverterServiceImpl(true));
        
        byte[] initial = new byte[]{1, 2, 3};
        Object result = converterBlock.convertResultSet(new MockSingleRowResultSet(new String[]{"content"}, new Object[]{initial}), null);
        assertEquals(initial, result);
    }
}
