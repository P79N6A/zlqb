package com.tasfe.sis.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Lait on 2017/7/17.
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role_resources")
public class RoleResources {
    /*@Id
    private Long id;*/
    // 角色id
    private Long roleId;
    // 资源id
    private Long resourceId;
}
