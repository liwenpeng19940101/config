package com.starv.controller;

import com.starv.entity.Config;
import com.starv.entity.Desc;
import com.starv.entity.Log;
import com.starv.util.Result;
import com.starv.util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.lang.reflect.Field;

/**
 * Created by m1590 on 2018/8/14.
 */
@RestController
@RequestMapping(value = "/config")
public class ConfigController {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    LogController logController;


    public static final Logger logger= LoggerFactory.getLogger(ConfigController.class);

    @Transactional
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public Result add(@RequestBody@Valid Config config, BindingResult bindingResult,HttpServletRequest request){
        if (bindingResult.hasErrors()){
            return ResultUtil.ERROR(bindingResult.getFieldError().getDefaultMessage());

        }
        try {

            String getCount=" select count(1) from collect_config ";
            Long count = jdbcTemplate.queryForObject(getCount, Long.class);
            if (count<1){

                StringBuilder sql=new StringBuilder();
                sql.append("insert into collect_config " +
                        "(send_flag,send_port,send_address,heartbeat_interval,loop_interval,cache_flag,cache_size,cache_num)" +
                        "values" +
                        "(?,?,?,?,?,?,?,?)");

                jdbcTemplate.update(sql.toString(),
                        config.getSend_flag(),
                        config.getSend_port(),
                        config.getSend_address(),
                        config.getHeartbeat_interval(),
                        config.getLoop_interval(),
                        config.getCache_flag(),
                        config.getCache_size(),
                        config.getCache_num()
                );
                /*记录到日志中*/
                HttpSession session = request.getSession();
                int user_id = (Integer)session.getAttribute("user_id");
                String user_name=session.getAttribute("user_name")+"";
                Log log=new Log();
                log.setUser_id(user_id);
                log.setModel_name("config");
                log.setContent(user_name+" insert  "+config.toString());
                logger.info(user_name+" insert  "+config.toString());
                logController.add(log);
                logger.info("新增采集配置已记录到日志");

                return ResultUtil.SUCCESS("采集配置添加成功");
            }else {

                Config befor_config = jdbcTemplate.queryForObject("select * from collect_config", (resultSet, i) -> {

                    Config cf = new Config();
                    cf.setId(resultSet.getInt("id"));
                    cf.setSend_flag(resultSet.getInt("send_flag"));
                    cf.setSend_port(resultSet.getInt("send_port"));
                    cf.setSend_address(resultSet.getString("send_address"));
                    cf.setHeartbeat_interval(resultSet.getInt("heartbeat_interval"));
                    cf.setLoop_interval(resultSet.getInt("loop_interval"));
                    cf.setCache_flag(resultSet.getInt("cache_flag"));
                    cf.setCache_size(resultSet.getInt("cache_size"));
                    cf.setCache_num(resultSet.getInt("cache_num"));

                    return cf;
                });

                StringBuilder sql=new StringBuilder();
                sql.append("update collect_config set send_flag = ?,send_port = ?,send_address = ?," +
                        "heartbeat_interval = ?,loop_interval = ?,cache_flag = ?, cache_size = ?,cache_num = ?");

                jdbcTemplate.update(sql.toString(),
                        config.getSend_flag(),
                        config.getSend_port(),
                        config.getSend_address(),
                        config.getHeartbeat_interval(),
                        config.getLoop_interval(),
                        config.getCache_flag(),
                        config.getCache_size(),
                        config.getCache_num()
                );

                /*记录到日志中*/
                HttpSession session = request.getSession();
                int user_id = (Integer)session.getAttribute("user_id");
                String user_name=session.getAttribute("user_name")+"";
//                int user_id=1;
//                String user_name="admin";
                Log log=new Log();
                log.setUser_id(user_id);
                log.setModel_name("config");
                StringBuilder diffent=new StringBuilder();
                diffent.append(user_name).append(" update ");
                /*反射获得所有字段*/
                Class configClass = befor_config.getClass();
                Field[] fields = configClass.getDeclaredFields();
                for(Field f:fields){
                    f.setAccessible(true);
                    Desc annotation = f.getAnnotation(Desc.class);
                    Object bo = f.get(befor_config);
                    Object o = f.get(config);
                    if (!bo.equals(o)){
                        diffent.append( annotation.value() +" 从 "+ bo + " 更新为 " + o );
                    }
                }
//                if (!befor_config.getSend_flag().equals(config.getSend_flag())){
//                    diffent.append("上报开关 "+befor_config.getSend_flag()+" 为 "+config.getSend_flag());
//                }
//                if (!befor_config.getSend_port().equals(config.getSend_port())){
//                    diffent.append("上报端口 "+befor_config.getSend_port()+" 为 "+config.getSend_port());
//                }
//                if (!befor_config.getSend_address().equals(config.getSend_address())){
//                    diffent.append("上报地址 "+befor_config.getSend_address()+" 为 "+config.getSend_address());
//                }
//                if (!befor_config.getHeartbeat_interval().equals(config.getHeartbeat_interval())){
//                    diffent.append("心跳间隔 "+befor_config.getHeartbeat_interval()+" 为 "+config.getHeartbeat_interval());
//                }
//                if (!befor_config.getLoop_interval().equals(config.getLoop_interval())){
//                    diffent.append("轮询间隔 "+befor_config.getLoop_interval()+" 为 "+config.getLoop_interval());
//                }
//                if (!befor_config.getCache_flag().equals(config.getCache_flag())){
//                    diffent.append("缓存开关 "+befor_config.getCache_flag()+" 为 "+config.getCache_flag());
//                }
//                if (!befor_config.getCache_size().equals(config.getCache_size())){
//                    diffent.append("缓存大小 "+befor_config.getCache_size()+" 为 "+config.getCache_size());
//                }
//                if (!befor_config.getCache_num().equals(config.getCache_num())){
//                    diffent.append("缓存条数 "+befor_config.getCache_num()+" 为 "+config.getCache_num());
//                }
                log.setContent(diffent.toString());

                logger.info(diffent.toString());
                logController.add(log);
                logger.info("更新采集配置已记录到日志");

                return ResultUtil.SUCCESS("采集配置更新成功");
            }

        }catch (Exception e){
            logger.error("配置信息添加或更新失败",e);
            return ResultUtil.ERROR(" 配置信息添加或更新失败");
        }


    }

    @RequestMapping(value = "/get",method = {RequestMethod.GET,RequestMethod.POST})
    public Result get(){
        try {
            StringBuilder sql_count=new StringBuilder();
            sql_count.append("select count(1) from collect_config");
            Integer count = jdbcTemplate.queryForObject(sql_count.toString(), Integer.class);
            if (count<1){
                return ResultUtil.ERROR("采集配置尚未配置数据");
            }

            StringBuilder sql=new StringBuilder();
            sql.append("select * from collect_config");

            Config rsm = jdbcTemplate.queryForObject(sql.toString(), (resultSet, i) -> {

                Config config = new Config();
                config.setId(resultSet.getInt("id"));
                config.setSend_flag(resultSet.getInt("send_flag"));
                config.setSend_port(resultSet.getInt("send_port"));
                config.setSend_address(resultSet.getString("send_address"));
                config.setHeartbeat_interval(resultSet.getInt("heartbeat_interval"));
                config.setLoop_interval(resultSet.getInt("loop_interval"));
                config.setCache_flag(resultSet.getInt("cache_flag"));
                config.setCache_size(resultSet.getInt("cache_size"));
                config.setCache_num(resultSet.getInt("cache_num"));

                return config;
            });
            return ResultUtil.SUCCESS(rsm);

        }catch (Exception e){
            logger.error("采集配置获取失败",e);
           return ResultUtil.ERROR("采集配置获取失败");
        }

    }

}
