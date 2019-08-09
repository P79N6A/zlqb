package com.nyd.zeus.service.batch;

import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cong Yuxiang
 * 2017/11/25
 **/
public class CalcuteFileWriter<T> implements ItemWriter<T> {

    private ItemWriter itemWriter;

    public void setItemWriter(FlatFileItemWriter itemWriter) {
        this.itemWriter = itemWriter;
    }

    @Override
    public void write(List<? extends T> items) throws Exception {
        Map<String,ReconciliationWsm> map = new HashMap<>();
        List<ReconciliationWsm> result = new ArrayList<>();
        for(int i = 0;i<items.size();i++){
            ReconciliationWsm reconciliationWsm = (ReconciliationWsm)items.get(i);
            String orderNo = reconciliationWsm.getOrderNo();
//            String key = orderNo+"_"+reconciliationWsm.getFlag();//防止重复
            if(map.get(orderNo)==null){
//
                map.put(orderNo,reconciliationWsm);
            }else {
                ReconciliationWsm twoWsm = map.get(orderNo);
                if(reconciliationWsm.getFlag().equals(twoWsm.getFlag())){
                    continue;
                }else {
                    if (Double.parseDouble(reconciliationWsm.getAmount()) == Double.parseDouble(twoWsm.getAmount())){
                        if(reconciliationWsm.getFlag().equals("1")){
                            reconciliationWsm.setFundCode(twoWsm.getFundCode());
                            reconciliationWsm.setAmountOwn(twoWsm.getAmount());
                            reconciliationWsm.setRemitStatus(twoWsm.getRemitStatus());
                            reconciliationWsm.setResultCode("0");
                            result.add(reconciliationWsm);
                        }else{
                            twoWsm.setFundCode(reconciliationWsm.getFundCode());
                            twoWsm.setAmountOwn(reconciliationWsm.getAmount());
                            twoWsm.setRemitStatus(reconciliationWsm.getRemitStatus());
                            twoWsm.setResultCode("0");
                            result.add(twoWsm);
                        }
                    }else{
                        if(reconciliationWsm.getFlag().equals("1")){
                            reconciliationWsm.setFundCode(twoWsm.getFundCode());
                            reconciliationWsm.setAmountOwn(twoWsm.getAmount());
                            reconciliationWsm.setRemitStatus(twoWsm.getRemitStatus());
                            reconciliationWsm.setResultCode("3");
                            result.add(reconciliationWsm);
                        }else{
                            twoWsm.setFundCode(reconciliationWsm.getFundCode());
                            twoWsm.setAmountOwn(reconciliationWsm.getAmount());
                            twoWsm.setRemitStatus(reconciliationWsm.getRemitStatus());
                            twoWsm.setResultCode("3");
                            result.add(twoWsm);
                        }
                    }
                    map.remove(orderNo);
                }
            }
        }

        for(Map.Entry<String,ReconciliationWsm> entry:map.entrySet()){
            ReconciliationWsm reconciliationWsm = entry.getValue();
            if(reconciliationWsm.getFlag().equals("1")){
                reconciliationWsm.setResultCode("2");
                result.add(reconciliationWsm);
            }else {
                reconciliationWsm.setResultCode("1");
                reconciliationWsm.setAmountOwn(reconciliationWsm.getAmount());
                reconciliationWsm.setAmount(null);
                result.add(reconciliationWsm);
            }
        }
        itemWriter.write(result);
    }

//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//
//    }

//    @Override
//    public void setResource(Resource resource) {
//
//    }
}
