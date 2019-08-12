package com.tasfe.sis.auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import java.util.List;

/**
 * Created by Lait on 2017/7/31.
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RoleResourcesInfos {

    private Long id;
    // 角色id
    private Long roleId;
    // 资源id
    private List<Long> resourcesIds;

}
