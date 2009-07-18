package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

/**
 * Key for finding IParameterSetter
 */
public class ParameterSetterKey {

    /**
     * Create key
     * @param aDataType   sql data type
     * @param aReturnType return type
     */
    public ParameterSetterKey(int aDataType, Class aReturnType) {
        theDataType = aDataType;
        theReturnType = aReturnType;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParameterSetterKey that = (ParameterSetterKey) o;

        if (theDataType != that.theDataType) return false;
        if (!theReturnType.equals(that.theReturnType)) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = theDataType;
        result = 31 * result + theReturnType.hashCode();
        return result;
    }

    public String toString() {
        return "ParameterSetterKey["+theDataType+":"+theReturnType.getSimpleName()+"]";
    }

    private final int theDataType;
    private final Class theReturnType;
}
