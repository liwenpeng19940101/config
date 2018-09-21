package com.starv.entity;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Created by m1590 on 2018/8/17.
 * create table  system_log(
 user_id bigint
 comment '用户id',
 create_time datetime not null
 comment '创建时间',
 model_name varchar(10) not null
 comment '模块名称',
 content varchar(300) not null
 comment '日志内容'
 );
 */

@Data
public class Log {
    @NotNull
    private Integer user_id;
    private String create_time;
    @NotEmpty
    private String model_name;
    @NotEmpty
    private String content;
}
