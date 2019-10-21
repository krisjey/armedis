
package com.github.armedis.http.service;

import com.github.armedis.config.ConstantNames;

import io.netty.util.AsciiString;

public class CommonHeader {
    public static final AsciiString HTTP_REQUEST_START = AsciiString.of(ConstantNames.HTTP_REQUEST_START);

    public static final AsciiString SERVICE_TRANSACTION_SEQ = AsciiString.of(ConstantNames.SERVICE_TRANSACTION_SEQ);

    public static final AsciiString ARMEDIS_INSTANCE_INFO = AsciiString.of(ConstantNames.ARMEDIS_INSTANCE_INFO);
}