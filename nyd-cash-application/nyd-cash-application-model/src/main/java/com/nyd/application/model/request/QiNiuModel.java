package com.nyd.application.model.request;

import java.io.Serializable;
import java.util.List;

import com.nyd.application.model.BaseInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class QiNiuModel extends BaseInfo implements Serializable{
	
    //七牛云图片上传空间名
    String bucket;
    //上传的图片编号
    List<String> keys;

}
