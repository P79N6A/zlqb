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


import com.creativearts.nyd.pay.service.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;


/**
 * Cong Yuxiang
 * 2017/11/20
 **/
public class DiskLog<V> {

    static final int LOG_FILE_HEADER_SIZE = 25;
    static final int LOG_RECORD_HEADER_SIZE = 17;


    static final Object obj = new Object();


    private static final Logger LOGGER = LoggerFactory.getLogger(DiskLog.class);




    private final Path LogPath;
    private final int maxLogFileSize;
    private final LogNamingStrategy namingStrategy;
    private final LogEntryWriter<V> writer;
    private final LogEntryReader<V> reader;

    DiskLog(Path LogPath,int maxLogFileSize, LogEntryReader<V> reader, LogEntryWriter<V> writer,
            LogNamingStrategy namingStrategy)
            throws IOException {

        this.LogPath = LogPath;
        this.maxLogFileSize = maxLogFileSize;
        this.namingStrategy = namingStrategy;
        this.writer = writer;
        this.reader = reader;
        if (!Files.isDirectory(LogPath, LinkOption.NOFOLLOW_LINKS)) {
            throw new IllegalArgumentException("LogPath is not a directory");
        }


    }






    int getMaxLogFileSize() {
        return maxLogFileSize;
    }

    Path getLogPath() {
        return LogPath;
    }


    public DiskLogResponse<V> getEntry(){
        try{
            synchronized (obj) {
                DiskLogFile<V> LogFile = buildLogFile();
                Position position = LogFile.getRecord();
                DiskLogResponse<V> diskLogResponse = new DiskLogResponse();
                if(position==null){
                    diskLogResponse.setOver(true);
                    return diskLogResponse;
                }
                if(position.getData()==null){
                    return null;
                }
                SimpleLogEntry<V> entry = reader.readLogEntry(position.getData().getBytes());

                diskLogResponse.setData(entry.getValue());
                diskLogResponse.setStart((int)position.getStart());
                diskLogResponse.setLength((int)position.getLenth());
                diskLogResponse.setOver(false);
                return diskLogResponse;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public void deleteEntry(int start,int length){
        try{
            synchronized (obj) {
                DiskLogFile<V> LogFile = buildLogFile();
               LogFile.deleteRecord(start,length);
            }
        }catch (Exception e){
            e.printStackTrace();

        }
    }
    public void flushEntry(V entry) {
        try {
            synchronized (obj) {
//                DiskLogFile<V> LogFile = LogFiles.peek();
//                if (LogFile == null) {
                    // Replay not required or succeed so start new Log
                DiskLogFile<V> LogFile = buildLogFile();
//                    LogFiles.push(LogFile);
//                }

                DiskLogEntry<V> recordEntry = DiskLogIOUtils.prepareLogEntry(entry, writer);

                 LogFile.appendRecord(recordEntry);

            }
        } catch (IOException e) {
          e.printStackTrace();
        }
    }


    private DiskLogFile<V> buildLogFile()
            throws IOException {

        return buildLogFile(getMaxLogFileSize());
    }

    private DiskLogFile<V> buildLogFile(int maxLogFileSize)
            throws IOException {

//        long logNumber = nextLogNumber();
        String filename = namingStrategy.generate();
        File LogFile = new File(LogPath.toFile(), filename);
        return new DiskLogFile<>(this, LogFile,maxLogFileSize);
    }




}
