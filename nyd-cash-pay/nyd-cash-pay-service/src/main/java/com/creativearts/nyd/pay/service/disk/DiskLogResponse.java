package com.creativearts.nyd.pay.service.disk;

import lombok.Data;

/**
 * Cong Yuxiang
 * 2017/11/20
 **/
public @Data
class DiskLogResponse<V>{
    private V data;
    private int start;
    private int length;
    private boolean over;
}
