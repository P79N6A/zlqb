package com.creativearts.nyd.pay.service.utils;

import java.io.IOException;

/**
 * Cong Yuxiang
 * 2017/11/20
 **/
public interface LogEntryReader<V> {
    SimpleLogEntry<V> readLogEntry(byte[] data)
            throws IOException;
}
