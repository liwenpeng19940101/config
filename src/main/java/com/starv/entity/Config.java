package com.starv.entity;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 *  create table collect_config(
 id bigint auto_increment primary key ,
 send_flag int not null default 1
 comment '上报开关标志 0是关 1是开',
 send_port int not null
 comment '上报端口',
 send_address varchar(100) not null
 comment '上报地址',
 heartbeat_interval int not null
 comment '心跳间隔 分钟',
 loop_interval int not null
 comment '轮询间隔 分钟',
 cache_flag int not null
 comment '缓存开关标志 0是关 1是开',
 cache_size bigint not null
 comment '缓存数据大小 单位是kb',
 cache_num bigint not null
 comment '缓存条数'
 );
 * Created by m1590 on 2018/8/14.
 */

@Data
public class Config {
    private int id;
    @NotNull(message = "上报开关不能为空")
    @Desc("上报开关")
    private Integer send_flag;

    @NotNull(message = "上报端口不能为空")
    @Desc("上报端口")
    private Integer send_port;

    @NotEmpty(message = "上报地址不能为空")
    @Desc("上报地址")
    private String send_address;

    @NotNull(message = "心跳间隔不能为空")
    @Desc("心跳间隔")
    private Integer heartbeat_interval;

    @NotNull(message = "轮询间隔不能为空")
    @Desc("轮询间隔")
    private Integer loop_interval;

    @NotNull(message = "缓存开关标志不能为空")
    @Desc("缓存开关")
    private Integer cache_flag;

    @NotNull(message = "缓存数据大小不能为空")
    @Desc("缓存数据大小")
    private Integer cache_size;

    @NotNull(message = "缓存条数不能为空")
    @Desc("缓存条数")
    private Integer cache_num;

    @Override
    public String toString(){
        StringBuilder sb=new StringBuilder();
        sb.append("上报开关:").append(send_flag);
        sb.append("上报端口:").append(send_port);
        sb.append("上报地址:").append(send_address);
        sb.append("心跳间隔:").append(heartbeat_interval);
        sb.append("缓存开关:").append(cache_flag);
        sb.append("缓存数据大小:").append(cache_size);
        sb.append("缓存条数:").append(cache_num);
        return sb.toString();
    }
}
