package com.creativearts.nyd.pay.service.impl;

import com.creativearts.nyd.pay.model.response.PayResponseData;
import com.creativearts.nyd.pay.service.utils.LogEntry;
import com.creativearts.nyd.pay.service.utils.LogEntryWriter;
import com.creativearts.nyd.pay.service.utils.SimpleLogEntry;

import java.io.DataOutput;
import java.io.IOException;

/**
 * Cong Yuxiang
 * 2017/11/20
 **/
public class PayResponseDataWriter implements LogEntryWriter<PayResponseData>{
    @Override
    public void writeLogEntry(SimpleLogEntry<PayResponseData> entry, DataOutput out)
            throws IOException {
        PayResponseData record = entry.getValue();
        out.writeUTF(record.getTransactionNo());
//        out.writeUTF(record.getMethod());
        out.writeUTF("\n");
//        out.writeUTF(record.name);
    }

    @Override
    public int estimateRecordSize(LogEntry<PayResponseData> entry) {
        return 0;
    }

    @Override
    public boolean isRecordSizeEstimateable() {
        return false;
    }
}
