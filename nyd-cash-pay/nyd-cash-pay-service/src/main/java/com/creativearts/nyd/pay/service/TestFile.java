package com.creativearts.nyd.pay.service;

import com.alibaba.fastjson.JSON;
import com.creativearts.nyd.pay.model.response.PayResponseData;
import com.creativearts.nyd.pay.service.disk.DiskLog;
import com.creativearts.nyd.pay.service.disk.DiskLogConfiguration;
import com.creativearts.nyd.pay.service.disk.DiskLogFactory;
import com.creativearts.nyd.pay.service.disk.DiskLogResponse;
import com.creativearts.nyd.pay.service.impl.PayResponseDataNamingStrategy;
import com.creativearts.nyd.pay.service.impl.PayResponseDataReader;
import com.creativearts.nyd.pay.service.impl.PayResponseDataWriter;
import com.creativearts.nyd.pay.service.utils.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Random;

/**
 * Cong Yuxiang
 * 2017/11/20
 **/
public class TestFile {
    public static void main(String[] args) throws IOException {
        File path = prepareLogDirectory("appendEntries");
        LogConfiguration<PayResponseData> configuration = buildDiskLogConfiguration(path.toPath(), 1024 * 1024,
                new PayResponseDataReader(), new PayResponseDataWriter(), new PayResponseDataNamingStrategy()
               );
        DiskLogFactory factory = DiskLogFactory.defaultInstance();
        DiskLog<PayResponseData> diskLog = factory.buildLogInfo(configuration);
//        SimpleLogEntry<PayResponseData> record1 = buildTestRecord(1, "test1", (byte) 12);
//        SimpleLogEntry<PayResponseData> record2 = buildTestRecord(2, "test2", (byte) 24);
//        SimpleLogEntry<PayResponseData> record3 = buildTestRecord(4, "test3", (byte) 32);
//        SimpleLogEntry<PayResponseData> record4 = buildTestRecord(8, "test4", (byte) 48);
//
//        diskLog.flushEntry(record1.getValue());
//        diskLog.flushEntry(record2.getValue());
//        diskLog.flushEntry(record3.getValue());
//        diskLog.flushEntry(record4.getValue());
        while (true) {
            DiskLogResponse<PayResponseData> data = diskLog.getEntry();
            System.out.println(JSON.toJSONString(data));
            if(data.isOver()){
                System.out.println("over");
                break;
            }
            diskLog.deleteEntry(data.getStart(), data.getLength());
        }
//        diskLog.

//        logInfo.close();
//
//        CountingFlushListener listener = new CountingFlushListener();
//        configuration.setListener(listener);
//        logInfo = simpleLogSystem.getLogInfo("appendEntries", configuration);
//
//        logInfo.close();

    }
    private static SimpleLogEntry<PayResponseData> buildTestRecord(int value, String name, byte type) {
        Random random = new Random(-System.nanoTime());

        int stringLength = random.nextInt(100);
//        StringBuilder sb = new StringBuilder(stringLength);
//        for (int i = 0; i < stringLength; i++) {
//            int charPos = random.nextInt(TESTCHARACTERS.length());
//            sb.append(TESTCHARACTERS.toCharArray()[charPos]);
//        }

        PayResponseData record = new PayResponseData();
        record.setTransactionNo(value + random.nextInt(1000000)+"");
//        record.setMethod(name);
//        record.value = value + random.nextInt(1000000);
//        record.name = name + "-" + sb.toString();
        return new SimpleLogEntry<>(record);
    }

    private static File prepareLogDirectory(String name)
            throws IOException {

        File path = new File("target/cyx/" + name);
        if (path.exists()){

        }else {
            path.mkdirs();
        }


        return path;
    }

    private static <V> LogConfiguration<V> buildDiskLogConfiguration(Path LogingPath, int maxLogFileSize,
                                                                              LogEntryReader<V> reader,
                                                                              LogEntryWriter<V> writer,
                                                                              LogNamingStrategy namingStrategy) {

        DiskLogConfiguration<V> configuration = new DiskLogConfiguration<>();
        configuration.setEntryReader(reader);
        configuration.setEntryWriter(writer);
        configuration.setLogPath(LogingPath);

        configuration.setMaxLogFileSize(maxLogFileSize);
        configuration.setNamingStrategy(namingStrategy);


        return configuration;
    }



}
