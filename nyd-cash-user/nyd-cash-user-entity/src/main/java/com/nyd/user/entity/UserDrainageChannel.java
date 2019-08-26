package com.nyd.user.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import javax.persistence.Table;

/**
 * <p>
 *      渠道表
 * </p>
 *
 * @author san
 * @since 2019-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "t_user_drainage_channel")
public class UserDrainageChannel implements Serializable {

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
