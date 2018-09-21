package com.starv.entity;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * Created by m1590 on 2018/8/14.
 */

@Data
public class User {
    private Integer user_id;
    @NotEmpty(message = "用户密码不能为空")
    private String password;
    private String create_time;
    @NotEmpty(message = "用户名不能为空")
    private String user_name;
}
