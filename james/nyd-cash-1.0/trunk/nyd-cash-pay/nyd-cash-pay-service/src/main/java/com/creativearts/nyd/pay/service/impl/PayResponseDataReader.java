package com.creativearts.nyd.pay.service.impl;

import com.creativearts.nyd.pay.model.response.PayResponseData;
import com.creativearts.nyd.pay.service.utils.LogEntryReader;
import com.creativearts.nyd.pay.service.utils.SimpleLogEntry;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * Cong Yuxiang
 * 2017/11/20
 **/
public class PayResponseDataReader implements LogEntryReader<PayResponseData> {

    @Override
    public SimpleLogEntry<PayResponseData> readLogEntry( byte[] data)
            throws IOException {
        try (ByteArrayInputStream in = new ByteArrayInputStream(data);
             DataInputStream buffer = new DataInputStream(in)) {
            String value = buffer.readUTF();
            String name = buffer.readUTF();
            PayResponseData record = new PayResponseData();
            record.setTransactionNo(value);
//            record.setMethod(name);
//            record.setTransactionNo();
//            record.value = value;
//            record.name = name;
            return new SimpleLogEntry<>(record);
        }
    }
}
