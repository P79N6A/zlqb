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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Cong Yuxiang
 * 2017/11/20
 **/
public class DiskLogFile<V>{

    private final Logger LOGGER = LoggerFactory.getLogger(DiskLogFile.class);

    private final Lock appendLock = new ReentrantLock();

    private final DiskLog<V> LOG;
    private final RandomAccessFile raf;
    private final String fileName;
    private  File file;

    DiskLogFile(DiskLog<V> LOG, File file, int maxLogFileSize)
            throws IOException {

        this(LOG, file,  maxLogFileSize, true);
    }

    DiskLogFile(DiskLog<V> LOG, File file, int maxLogFileSize, boolean createIfNotExisting)
            throws IOException {

        if (!file.exists() && !createIfNotExisting) {
            throw new RuntimeException("File " + file.getAbsolutePath() + " does not exists and creation is forbidden");
        }

        this.LOG = LOG;
        this.fileName = file.getName();
        this.file = file;
        this.raf = new RandomAccessFile(file, "rw");
    }

    DiskLogFile(RandomAccessFile raf, String fileName,DiskLog<V> LOG) {
        this.raf = raf;
        this.fileName = fileName;
        this.LOG = LOG;
    }



    String getFileName() {
        return fileName;
    }
    void deleteRecord(int start,int lenth) throws IOException {
        try {
            raf.seek(start);
            byte[] bytes = new byte[lenth];
            raf.write(bytes);
        }catch (Exception e){

        }finally {
            raf.close();
        }
    }
    Position getRecord() throws IOException {

        Position position = new Position();
        try {
            position.setStart(raf.getFilePointer());
//        raf.readLine();
//        raf.readUTF()
            String s = raf.readLine();
            if (s == null) {
                raf.close();
                file.delete();
                return null;
            }
            while (s.trim().length() == 0) {
                position.setStart(raf.getFilePointer());
                s = raf.readLine();
                if (s != null && s.trim().length() > 0) {
                    break;
                } else if (s == null) {
                    raf.close();
                    file.delete();
                    return null;
                }
            }
            position.setData(s);
            position.setLenth(raf.getFilePointer() - 1 - position.getStart());

            System.out.println(s.getBytes().length);
            return position;
        }catch (Exception e){
            e.printStackTrace();
            return new Position();
        }finally {

            raf.close();
        }
    }

    void appendRecord(DiskLogEntry<V> entry)
            throws IOException {

        long nanoSeconds = 0;
        if (LOGGER.isDebugEnabled()) {
            nanoSeconds = System.nanoTime();
        }

        try {
            appendLock.lock();

            byte[] entryData = entry.cachedData;

            DiskLogIOUtils.writeRecord(entryData, raf);



        } finally {
            raf.close();
            appendLock.unlock();
            LOGGER.trace("DiskLogFile::appendRecord took {}ns", (System.nanoTime() - nanoSeconds));
        }
    }







    private int getPosition() {
        try {
            return (int) raf.getFilePointer();
        } catch (IOException e) {
            return -1;
        }
    }


}
