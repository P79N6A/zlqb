package com.nyd.application.service.consts;

/**
 * Created by Dengw on 2017/12/11
 */
public class ApplicationConsts {
    public static String ERROR_MSG = "服务器开小差了,请稍后再试";

    public interface RET_STATUS {
        public final static String SUCCESS = "0";
        public final static String FAIL = "1";
    }

    public interface ERROR_CODE {
        public final static String SUCCESS = "00000000";
        public final static String UNKOWN = "84000001";

        /**
         * 调用外部rpc失败
         */
        public final static String RPC_ERROR = "F5000001";
    }

    public interface RET_MSG {
        public final static String OK = "调用成功";
        public final static String FAIL = "调用失败";
    }
    
    //网签合同只有一份会签约第二家网签公司(2019/08/06)
    public interface COMPANY_CLASS_NAME {
        //验签a公司-old(介入的第一家网签公司)
        public final static String companyNameNewUpToDateA = "大庆市语诗铭科技有限公司";
        
        //验签b公司-new(介入的第二家网签公司)
        public final static String companyNameNewUpToDateB = "";
    }
    
}
