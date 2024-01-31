package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

class ParameterConverterUtils {
    static String filter3BytesUTF(String value) {
        if (value == null) {
            return null;
        }

        if (!has4ByteChars(value)) {
            return value;
        }

        StringBuilder builder = new StringBuilder(value.length());

        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (!Character.isSurrogate(c)) {
                builder.append(c);
            }
        }

        return builder.toString();
    }

    private static boolean has4ByteChars(String value) {
        for (int i = 0; i < value.length(); i++) {
            if (Character.isSurrogate(value.charAt(i))) {
                return true;
            }
        }

        return false;
    }
}
