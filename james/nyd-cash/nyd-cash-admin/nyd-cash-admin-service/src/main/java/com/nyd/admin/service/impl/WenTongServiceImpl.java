package com.nyd.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.nyd.admin.model.WenTongExcelVo;
import com.nyd.admin.service.WenTongService;
import com.nyd.admin.service.utils.BankPlaceUtil;
import com.nyd.capital.api.service.RemitService;
import com.nyd.capital.model.wt.WtCallbackResponse;
import com.nyd.order.api.OrderContract;
import com.nyd.order.api.OrderWentongContract;
import com.nyd.order.model.OrderInfo;
import com.nyd.order.model.OrderWentongInfo;
import com.nyd.user.api.UserContactContract;
import com.nyd.user.api.UserIdentityContract;
import com.nyd.user.api.UserInfoContract;
import com.nyd.user.api.UserJobContract;
import com.nyd.user.model.ContactInfos;
import com.nyd.user.model.JobInfo;
import com.nyd.user.model.UserDetailInfo;
import com.nyd.user.model.UserInfo;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class WenTongServiceImpl implements WenTongService {
    static  Logger logger = LoggerFactory.getLogger(WenTongServiceImpl.class);
    @Autowired
    OrderContract orderContract;
    @Autowired
    OrderWentongContract orderWentongContract;
    @Autowired
    UserJobContract userJobContract;
    @Autowired
    UserInfoContract userInfoContract;
    @Autowired
    UserContactContract userContactContract;
    @Autowired
    UserIdentityContract userIdentityContract;
    @Autowired
    RemitService remitService;

    @Override
    public List<WenTongExcelVo> queryWenTongExcelVo(String startDate, String endDate, String username, String mobile) {

        ResponseData<List<OrderWentongInfo>> orderWTByTime = orderWentongContract.getOrderWTByTime(startDate, endDate, username, mobile);
        List<OrderWentongInfo> info = orderWTByTime.getData();

        logger.info("查询出的结果为"+ JSON.toJSONString(info));

//        List<OrderWentongInfo> info = new ArrayList<>();
//        OrderWentongInfo info1 = new OrderWentongInfo("101516223720470001", "180180400005", "15736006889", "", "");
//        OrderWentongInfo info2 = new OrderWentongInfo("101516225705772001", "173611600203", "13714394802", "", "");
//        OrderWentongInfo info3 = new OrderWentongInfo("101516226963976001", "180180500004", "18671111920", "", "");
//        info.add(info1);
//        info.add(info2);
//        info.add(info3);
        List<WenTongExcelVo> list = new ArrayList<>();
        try {

            for (OrderWentongInfo orderWentongInfo : info) {

                //获取用户详细信息
                UserDetailInfo userDetailInfo = userIdentityContract.getUserDetailInfo(orderWentongInfo.getUserId()).getData();

                //获取用户基本信息
              /*  UserDto userDto = new UserDto();
                userDto.setAccountNumber(orderWentongInfo.getMobile());
                userDto.setIdNumber(userDetailInfo.getIdNumber());*/
                UserInfo userInfo = userIdentityContract.getUserInfo(orderWentongInfo.getUserId()).getData();


                //获取用户的联系人信息
                ContactInfos contactInfo = userContactContract.getContactInfo(orderWentongInfo.getUserId()).getData();

                //获取用户的工作信息
                JobInfo jobInfo = userJobContract.getJobInfo(orderWentongInfo.getUserId()).getData();

                String orderNo = orderWentongInfo.getOrderNo().substring(0,orderWentongInfo.getOrderNo().length()-1);
                //获取用户的银行卡信息
                OrderInfo orderInfo = orderContract.getOrderByOrderNo(orderNo).getData();

                String province = null;
                String city = null;
                String bankPlace = BankPlaceUtil.getBankPlace(orderInfo.getBankAccount());
                if (StringUtils.isNotBlank(bankPlace)) {
                    province = bankPlace.split(" - ")[0].split("省")[0];
                    city = bankPlace.split(" - ")[1].split("市")[0];
                } else {
                    province = userDetailInfo.getIdAddress().split("省")[0];
                    city = userDetailInfo.getIdAddress().split("市")[0].split("省")[1].split("市")[0];
                }
                WenTongExcelVo wenTongExcelVo = new WenTongExcelVo();

                //姓名
                String name = userDetailInfo.getRealName();
                wenTongExcelVo.setCustomerName(name);
                wenTongExcelVo.setOrderNo(orderWentongInfo.getOrderNo());
                //性别
                wenTongExcelVo.setSex(userInfo.getGender());
                //年龄
                int age = getAgeByBirth(userInfo.getBirth());
                wenTongExcelVo.setAge(age);
                //身份证号
                wenTongExcelVo.setIDNumber(userDetailInfo.getIdNumber());
                //移动电话
                wenTongExcelVo.setMobile(orderWentongInfo.getMobile());
                //婚姻状态
                String marital = userDetailInfo.getMaritalStatus();
                wenTongExcelVo.setMaritalStatus(marital);
                //户口所在地
                wenTongExcelVo.setDomicilePlace(userDetailInfo.getIdAddress().split("市")[0] + "市");
                //学历
                wenTongExcelVo.setEducation(userDetailInfo.getHighestDegree());
                //现住宅地址
                String address = userDetailInfo.getLivingAddress();
                wenTongExcelVo.setAddress(address);
                //单位名称
                String company = jobInfo.getCompany();
                wenTongExcelVo.setCompany(company);
                //职位级别
                String profession = jobInfo.getProfession();
                wenTongExcelVo.setPosition(profession);
                //单位地址
                wenTongExcelVo.setCompanyAddress(jobInfo.getCompanyAddress());
                //工作薪资
                String salary = jobInfo.getSalary();
                if (salary.contains("2000以下")) {
                    salary = "1800";
                } else if (salary.contains("2000-3000元")) {
                    salary = "2500";
                } else if (salary.contains("3001-5000元")) {
                    salary = "4000";
                } else if (salary.contains("5001-8000元")) {
                    salary = "6500";
                } else if (salary.contains("8001-12000元")) {
                    salary = "10000";
                } else if (salary.contains("12001-15000元")) {
                    salary = "13500";
                } else if (salary.contains("15001-18000元")) {
                    salary = "17000";
                } else if (salary.contains("18000以上")) {
                    salary = "180000";
                }
                wenTongExcelVo.setSalary(salary);
                //业余收入
                wenTongExcelVo.setAmateurSalary(0);
                //月总收入
                wenTongExcelVo.setTotalSalary(salary);
                //姓名1(其他联系人)
                wenTongExcelVo.setOtherName1(contactInfo.getDirectContactName());
                //移动电话1
                wenTongExcelVo.setOtherPhone1(contactInfo.getDirectContactMobile());
                //关系1
                wenTongExcelVo.setRelation1(contactInfo.getDirectContactRelation());
                //姓名2(其他联系人)
                wenTongExcelVo.setOtherName2(contactInfo.getMajorContactName());
                //移动电话2
                wenTongExcelVo.setOtherPhone2(contactInfo.getMajorContactMobile());
                //关系2
                wenTongExcelVo.setRelation2(contactInfo.getMajorContactRelation());
                //银行卡号
                wenTongExcelVo.setCardNO(orderInfo.getBankAccount());
                //开户行名
                wenTongExcelVo.setBankName(orderInfo.getBankName());
                //开户省
                wenTongExcelVo.setBankProvince(province);
                //开户市/县
                wenTongExcelVo.setBankCity(city);
                //申请金额
                BigDecimal amount = orderInfo.getRealLoanAmount();
                wenTongExcelVo.setAppliedAmount(amount);
                //申请期限
                wenTongExcelVo.setProjectPeriod(orderInfo.getBorrowPeriods());
                //借款用途
                String purpose = orderInfo.getLoanPurpose();
                wenTongExcelVo.setPurpose(purpose);
                //风控描述
                wenTongExcelVo.setDescribe("借款人:" + name + "; 年龄:" + age + "岁; 婚姻:" + marital + "; 居住所在地:" + address.split("市")[0] + "; 月均收入:" + salary + "元; 借款金额:" + amount.intValue() + "元; 借款人基本情况:" + name + "为" + (StringUtils.isNotBlank(company)?company+"的":"") + profession + ",收入稳定,信用良好; 借款用途:" + purpose + ";借款方还款来源:工资.");
                //证件

                list.add(wenTongExcelVo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public ResponseData importExcel(CommonsMultipartFile file) {
        List<WtCallbackResponse> list = new ArrayList<>();
        // FileInputStream file = new FileInputStream("C:/Users/Administrator/Desktop/放款清单模版.xlsx");
        XSSFRow row = null;
        try {
            XSSFWorkbook wb = new XSSFWorkbook(file.getInputStream());
            XSSFSheet xs = wb.getSheetAt(0);
            if (xs == null) {
                throw new RuntimeException("该文件是空的");
            }
            for (int i = 0; i < (xs.getPhysicalNumberOfRows() % 100 == 0 ? xs.getPhysicalNumberOfRows() / 100 : xs.getPhysicalNumberOfRows() / 100 + 1); i++) {
                for (int rowNum = 1; rowNum < (xs.getPhysicalNumberOfRows() < 100 ? xs.getPhysicalNumberOfRows() : 100); rowNum++) {
                    row = xs.getRow(rowNum);
                    if (row.getPhysicalNumberOfCells() < 9) {
                        break;
                    }

                    WtCallbackResponse vo = new WtCallbackResponse();
                    vo.setOrderNo(getValue(row.getCell(1)));
                    vo.setWithDrawNo(getValue(row.getCell(2)));
                    vo.setRecordNo(getValue(row.getCell(3)));
                    vo.setRemitTime(getValue(row.getCell(4)));
                    vo.setAmount(new BigDecimal(getValue(row.getCell(5))));
                    String status = getValue(row.getCell(9));
                    if("交易成功".equals(status)) {
                        vo.setStatus("0");
                    }else {
                        vo.setStatus("1");
                    }

                    list.add(vo);
                }
                remitService.wtLoan(list);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseData.error(e.getMessage());
        }
        return ResponseData.success();
    }

    //生日转年龄

    private static int getAgeByBirth(String birthday) {
        logger.info(birthday);
        int age = 0;
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());// 当前时间
        int nowYear = now.get(Calendar.YEAR);
        int year = Integer.parseInt(birthday.substring(0, birthday.indexOf("年")));
        age = nowYear - year;
        return age;
    }


    private static String getValue(XSSFCell xssfCell) {
        if (xssfCell.getCellType() == xssfCell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(xssfCell.getBooleanCellValue());
        } else if (xssfCell.getCellType() == xssfCell.CELL_TYPE_NUMERIC) {
            DecimalFormat df = new DecimalFormat("0");
            return String.valueOf(df.format(xssfCell.getNumericCellValue()));
        } else if (xssfCell.getCellType() == xssfCell.CELL_TYPE_STRING) {
            return String.valueOf(xssfCell.getStringCellValue());
        } else {
            return String.valueOf(xssfCell.getStringCellValue());
        }
    }

}
