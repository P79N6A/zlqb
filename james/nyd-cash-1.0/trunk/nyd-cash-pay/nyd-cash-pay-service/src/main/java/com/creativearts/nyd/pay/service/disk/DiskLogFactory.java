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
import org.apache.commons.logging.LogConfigurationException;

import java.io.IOException;
import java.nio.file.Path;
/**
 * Cong Yuxiang
 * 2017/11/20
 **/
public class DiskLogFactory<V>{

    public static final DiskLogFactory<?> DEFAULT_INSTANCE = new DiskLogFactory<>();

    private static final int MIN_DISK_LOG_FILE_SIZE = 1024;


    public DiskLog<V> buildLogInfo(LogConfiguration<V> configuration) {
        Preconditions.checkType(configuration, DiskLogConfiguration.class, "configuration");
        DiskLogConfiguration<V> diskConfig = (DiskLogConfiguration<V>) configuration;

        Path LogingPath = diskConfig.getLogPath();

        int maxLogFileSize = diskConfig.getMaxLogFileSize();

        LogEntryReader<V> entryReader = diskConfig.getEntryReader();
        LogEntryWriter<V> entryWriter = diskConfig.getEntryWriter();
        LogNamingStrategy namingStrategy = diskConfig.getNamingStrategy();

        Preconditions.notNull(LogingPath, "configuration.LogingPath");

        if (maxLogFileSize < MIN_DISK_LOG_FILE_SIZE) {
            throw new IllegalArgumentException("configuration.maxLogFileSize must not be below " + MIN_DISK_LOG_FILE_SIZE);
        }

        try {
            return new DiskLog<V>(LogingPath,maxLogFileSize, entryReader, entryWriter,
                    namingStrategy);

        } catch (IOException e) {
            throw new LogConfigurationException("Error while configuring the Log", e);
        }
    }

    public static <V> DiskLogFactory<V> defaultInstance() {
        return (DiskLogFactory<V>) DEFAULT_INSTANCE;
    }

}
