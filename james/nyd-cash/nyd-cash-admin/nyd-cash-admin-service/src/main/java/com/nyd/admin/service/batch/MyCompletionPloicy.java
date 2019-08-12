package com.nyd.admin.service.batch;

import org.springframework.batch.repeat.RepeatContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.repeat.policy.SimpleCompletionPolicy;

/**
 * Cong Yuxiang
 * 2017/11/25
 **/
public class MyCompletionPloicy extends SimpleCompletionPolicy{
    @Override
    public boolean isComplete(RepeatContext context, RepeatStatus result) {
      super.isComplete(context, result);
      if(result==RepeatStatus.FINISHED){
          return true;
      }else {
          return false;
      }
    }
}
