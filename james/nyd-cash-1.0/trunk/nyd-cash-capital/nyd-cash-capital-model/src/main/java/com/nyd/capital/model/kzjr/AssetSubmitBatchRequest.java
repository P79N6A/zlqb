package com.nyd.capital.model.kzjr;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Cong Yuxiang
 * 2017/12/11
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AssetSubmitBatchRequest implements Serializable{
    private String channelCode;
    private List<SubAsset> batchData;
    private String sign;


}
