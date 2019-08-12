package com.tasfe.sis.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;

/**
 * Created by Lait on 2017/7/16.
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResourcesAttrs {
    @Id
    private Long id;

    private String key;

    private String val;

    public ResourcesAttrs(String key, String val) {
        this.key = key;
        this.val = val;
    }
}
