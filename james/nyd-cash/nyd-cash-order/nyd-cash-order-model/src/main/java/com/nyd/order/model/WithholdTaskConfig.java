package com.nyd.order.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author liuqiu
 */
@Data
@ToString
@Table(name="t_withhold_task_config")
public class WithholdTaskConfig implements Serializable {
    private Date startTime;
    private String configCode;
}
