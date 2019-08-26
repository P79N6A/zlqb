package com.nyd.user.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *      渠道表
 * </p>
 *
 * @author san
 * @since 2019-08-26
 */
@Data
public class UserDrainageChannelVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id编号
     */
    @Id
    private Long id;

    /**
     * 引流渠道
     */
    private String drainageChannelName;

    /**
     * 0为开启 1为关闭
     */
    private Integer state;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createMan;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private String updateMan;
}
