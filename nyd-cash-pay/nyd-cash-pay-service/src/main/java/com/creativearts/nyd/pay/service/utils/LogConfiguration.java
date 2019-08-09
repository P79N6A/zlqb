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
package com.creativearts.nyd.pay.service.utils;


/**
 * Cong Yuxiang
 * 2017/11/20
 **/
public class LogConfiguration<V> {



    private LogNamingStrategy namingStrategy;

    private LogEntryReader<V> entryReader;

    private LogEntryWriter<V> entryWriter;



    public LogNamingStrategy getNamingStrategy() {
        return namingStrategy;
    }

    public void setNamingStrategy(LogNamingStrategy namingStrategy) {
        this.namingStrategy = namingStrategy;
    }

    public LogEntryReader<V> getEntryReader() {
        return entryReader;
    }

    public void setEntryReader(LogEntryReader<V> entryReader) {
        this.entryReader = entryReader;
    }

    public LogEntryWriter<V> getEntryWriter() {
        return entryWriter;
    }

    public void setEntryWriter(LogEntryWriter<V> entryWriter) {
        this.entryWriter = entryWriter;
    }

}
