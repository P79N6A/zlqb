package com.nyd.batch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AddressBook implements Serializable {
    private String phoneNo;
    private String name;
    private String deviceId;
    private String phoneOs;
    private String versionName;
    private String tel;
    private String updatetime;
    private Date create_time = new Date();
}
