package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import junit.framework.TestCase;

import java.nio.charset.StandardCharsets;

public class ParameterConverter_VARCHAR_StringTest extends TestCase {

    public void testFilter3BytesUTF() {
        String s3Bytes = "abc‱" + new String(new byte[] {(byte) 0xff, (byte) 0xff, (byte) 0xff}, StandardCharsets.UTF_8) + "def";
        String s4Bytes = "abc\uD83D\uDD8C\uD83D\uDC31def‱";

        assertEquals(s3Bytes, ParameterConverterUtils.filter3BytesUTF(s3Bytes));
        assertEquals("abcdef‱", ParameterConverterUtils.filter3BytesUTF(s4Bytes));
    }
}