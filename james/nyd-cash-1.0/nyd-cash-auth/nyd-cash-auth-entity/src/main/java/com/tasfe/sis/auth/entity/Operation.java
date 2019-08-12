package com.tasfe.sis.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;

/**
 * Created by Lait on 2017/7/14.
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Operation {
    @Id
    private Long id;

    // 角色id
    private Long rid;

    private String name;
    private String code;
    private String descr;

}
