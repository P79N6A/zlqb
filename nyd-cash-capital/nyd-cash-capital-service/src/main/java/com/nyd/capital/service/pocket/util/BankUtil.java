package com.nyd.capital.service.pocket.util;

import com.nyd.capital.model.enums.PocketBankEnum;

/**
 * 获取银行编码工具类
 * @author liuqiu
 */
public class BankUtil {

    public static int getBankCode(String bankName) {
        for (PocketBankEnum pocketBankEnum : PocketBankEnum.values()) {
            if (pocketBankEnum.getName().equals(bankName)) {
                return pocketBankEnum.getCode();
            }
        }
        return 0;
    }
}
