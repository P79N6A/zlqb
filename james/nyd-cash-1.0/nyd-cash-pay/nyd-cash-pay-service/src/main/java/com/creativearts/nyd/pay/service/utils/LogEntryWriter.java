package com.creativearts.nyd.pay.service.utils;

import java.io.DataOutput;
import java.io.IOException;

/**
 * Cong Yuxiang
 * 2017/11/20
 **/
public interface LogEntryWriter<V> {
    void writeLogEntry(SimpleLogEntry<V> entry, DataOutput out)
            throws IOException;

    int estimateRecordSize(LogEntry<V> entry);

    boolean isRecordSizeEstimateable();
}
