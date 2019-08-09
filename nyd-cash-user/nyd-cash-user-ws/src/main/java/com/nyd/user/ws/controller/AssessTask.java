package com.nyd.user.ws.controller;

import com.nyd.member.api.MemberContract;
import com.nyd.member.model.MemberModel;
import com.nyd.user.api.UserStepContract;
import com.nyd.user.model.StepInfo;
import com.nyd.user.service.consts.UserConsts;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("/user")
public class AssessTask {

    private static Logger LOGGER = LoggerFactory.getLogger(AssessTask.class);

    @Autowired
    private MemberContract memberContract;
    @Autowired
    private UserStepContract userStepContract;


    @RequestMapping(value = "/doAssessTask", method = RequestMethod.POST, produces = "application/json")
    public ResponseData doAssessTask() throws IOException {
        ResponseData responseData = ResponseData.success();
        List<String> list = new ArrayList<>();
        ResponseData<List<String>> listResponseData = memberContract.doAssessTask();
        try {
            if (listResponseData.getStatus().equals("0")) {
                List<String> memberModels = listResponseData.getData();
                LOGGER.info("总计有" + memberModels.size() + "个老会员未生成报告");
                int i = 0;
                for (String userId : memberModels) {
                    try {
                        ResponseData<StepInfo> userStep = userStepContract.doAssessTask(userId);
                        if (userStep == null || (!"0".equals(userStep.getStatus()))) {
                            list.add(userId);
                        }
                    } catch (Exception e) {
                        LOGGER.warn(e.getMessage());
                        LOGGER.info("第" + i + "个用户,userId为" + userId + "的会员的生成报告失败");
                        list.add(userId);
                    }
                    LOGGER.info("第" + i + "个用户,userId为" + userId + "的会员生成报告");
                    i++;
                }
                LOGGER.info("总计生成了" + i + "个会员的报告");
                return responseData.setData(i);
            } else {
                return ResponseData.error(UserConsts.DB_ERROR_MSG);
            }
        } finally {
            File file = new File("/data/logs/nyd/user/fail");
            //File f = new File("D://statics/fail");
            list.add("first");
            list.add("second");
            list.add("third");
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            for (int i = 0; i < list.size(); i++) {
                bw.write(list.get(i));
                bw.newLine();
            }
            bw.close();
        }
    }

//    public static void main(String[] args) {
//        List<String> list = new ArrayList<>();
//        try {
//            File f = new File("D://statics/fail");
//            list.add("first");
//            list.add("second");
//            list.add("third");
//            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
//            for (int i = 0; i < list.size(); i++) {
//                bw.write(list.get(i));
//                bw.newLine();
//            }
//            bw.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
