package com.nyd.user.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Table;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "t_account_reset")
public class AccountReset implements Serializable{
    @Id
    private Long id;
    private String old_account_number;
    private String new_account_number;
    private String user_id;
    private String i_bank_user_id;
    private Integer delete_flag;
    private Date create_time;
    private Date update_time;
}
