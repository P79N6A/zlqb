package com.nyd.application.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by Dengw on 2017/12/1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MessageModel implements Serializable {
    //手机号
    private String mobile;
    //留言信息
    private String content;
}
