package com.nyd.zeus.service.batch;

import com.nyd.zeus.service.util.HashUtils;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.support.AbstractItemStreamItemWriter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.FileSystemResource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cong Yuxiang
 * 2017/11/25
 **/

public class WsmMultiItemWriter<T> extends AbstractItemStreamItemWriter<T> implements ItemWriter<T>,ApplicationContextAware {

    private List<FlatFileItemWriter> delegates = new ArrayList<>();
    private Integer num;
    private ApplicationContext applicationContext;
    private String directoryPath;
//    public void setDelegates(List<ItemWriter<? super T>> delegates) {
//        this.delegates = delegates;
//    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public void setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
    }

//    @PostConstruct
//    public void initList(){
//        for (int i=0;i<num;i++) {
//            FlatFileItemWriter writer = (FlatFileItemWriter)applicationContext.getBean("fileWriter");
//            writer.setResource(new FileSystemResource(directoryPath+"/"+i+".csv"));
//            writer.open(new ExecutionContext());
//            delegates.add(writer);
//        }
//    }
    @Override
    public void write(List<? extends T> items) throws Exception {
        Map<Integer,List<ReconciliationWsm>> map = new HashMap<>();

        // 将传过来的信息按照不同的类型添加到不同的List中
        for (int i = 0; i < items.size(); i++) {
            ReconciliationWsm reconciliationWsm = (ReconciliationWsm)items.get(i);

            int hashKey = HashUtils.hash(reconciliationWsm.getOrderNo())%num;
            if(map.get(hashKey)==null){
                List<ReconciliationWsm> list = new ArrayList();
                list.add(reconciliationWsm);
                map.put(hashKey,list);
            }else {
                map.get(hashKey).add(reconciliationWsm);
            }

        }
        for(Map.Entry<Integer,List<ReconciliationWsm>> entry:map.entrySet()){
            delegates.get(entry.getKey()).write(entry.getValue());
        }


    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void close() {
        for(FlatFileItemWriter flatFileItemWriter:delegates){
            flatFileItemWriter.close();
        }
    }

    @Override
    public void open(ExecutionContext executionContext) {
        super.open(executionContext);
        for (int i=0;i<num;i++) {
            FlatFileItemWriter writer = (FlatFileItemWriter)applicationContext.getBean("fileWriter");
            writer.setResource(new FileSystemResource(directoryPath+"/"+i+".csv"));
            writer.open(new ExecutionContext());
            delegates.add(writer);
        }
    }

    @Override
    protected void setExecutionContextName(String name) {
        super.setExecutionContextName(name);
    }

    @Override
    public String getExecutionContextKey(String key) {
        return super.getExecutionContextKey(key);
    }
}
