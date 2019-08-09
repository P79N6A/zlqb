package com.nyd.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import javax.persistence.Table;
import java.util.Date;


/**
 * Created by hwei on 2017/11/1.
 * 用户附件信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "t_user_attachment")
public class Attachment {
    @Id
    private Long id;
    //用户ID
    private String userId;
    //附件类型
    private String type;
    //附件名称
    private String fileName;
    //文件路径
    private String filePath;
    //文件类型
    private String mediaType;
    //是否已删除
    private Integer deleteFlag;
    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //修改人
    private String updateBy;

}
