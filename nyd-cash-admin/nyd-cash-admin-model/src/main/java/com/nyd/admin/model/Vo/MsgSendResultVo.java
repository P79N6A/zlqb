package com.nyd.admin.model.Vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class MsgSendResultVo implements Serializable {

    private int totalCount;

    private int successCount;

    private int failCount;

    private List<String> failPhone;
}
