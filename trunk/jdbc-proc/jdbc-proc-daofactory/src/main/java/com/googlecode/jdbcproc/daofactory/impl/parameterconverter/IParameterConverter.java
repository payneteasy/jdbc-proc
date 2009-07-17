package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

/**
 * Sets parameter
 */
public interface IParameterConverter<T> {
    /**
     * Sets parameter to statement
     * @param aValue         value
     * @param aStmt          statement
     * @param aParameterName parameter name
     * @throws SQLException  on error
     */
    void setValue(T aValue, CallableStatement aStmt, String aParameterName) throws SQLException ;

    /**
     * Sets parameter to statement
     * @param aValue         value
     * @param aStmt          statement
     * @param aIndex         parameter index
     * @throws SQLException  on error
     */
    void setValue(T aValue, PreparedStatement aStmt, int aIndex) throws SQLException ;

    /**
     * Returns output parameter, converted to disired java type
     * @param aStmt          callable statement
     * @param aParameterName parameter name
     * @return               converted value
     * @throws SQLException on error
     */
    T getOutputParameter(CallableStatement aStmt, String aParameterName) throws SQLException;

    /**
     * Gets value from result set
     * @param aResultSet result set
     * @param aParameterName parameter name
     * @return converted value on error
     */
    T getFromResultSet(ResultSet aResultSet, String aParameterName) throws SQLException;


    /**
     * Key for finding
     * @return key
     */
    ParameterSetterKey getKey() ;

}
