/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package com.creativearts.nyd.pay.service.disk;


import com.creativearts.nyd.pay.service.utils.LogEntryWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
/**
 * Cong Yuxiang
 * 2017/11/20
 **/
enum DiskLogIOUtils {
    ;

    private static final Logger LOGGER = LoggerFactory.getLogger(DiskLogIOUtils.class);



    static <V> DiskLogEntry<V> prepareLogEntry(V obj, LogEntryWriter<V> writer)
            throws IOException {

        long nanoSeconds = 0;
        if (LOGGER.isDebugEnabled()) {
            nanoSeconds = System.nanoTime();
        }

        DiskLogEntry<V> LogEntry = buildDiskLog(obj);
        if (LogEntry.cachedData == null) {
            try (ByteArrayOutputStream out = new ByteArrayOutputStream(2000);
                 DataOutputStream stream = new DataOutputStream(out)) {

                writer.writeLogEntry(LogEntry, stream);
                LogEntry.cachedData = out.toByteArray();

            }

            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("DiskLogIOUtils::prepareLogEntry tool {}ns", (System.nanoTime() - nanoSeconds));
            }
        }

        return LogEntry;
    }

    private static <V> DiskLogEntry<V> buildDiskLog(V entry) {
        return new DiskLogEntry<>(entry);
    }

    static <V> void writeRecord( byte[] entryData, RandomAccessFile raf)
            throws IOException {



//        int minSize = DiskLog.LOG_RECORD_HEADER_SIZE + entryData.length;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream(entryData.length);
             DataOutputStream stream = new DataOutputStream(out)) {

//            int recordLength = DiskLog.LOG_RECORD_HEADER_SIZE + entryData.length;

//            stream.writeInt(recordLength);
//            stream.writeLong(record.getRecordId());
//            stream.writeByte(record.getType());
            raf.seek(raf.length());
            stream.write(entryData);
//            stream.writeInt(recordLength);
            raf.write(out.toByteArray());
//        raf.close();
//            raf.write(out.toByteArray());
        }

//        LOGGER.trace("DiskLogIOUtils::writeRecord took {}ns", (System.nanoTime() - nanoSeconds));
    }



}
