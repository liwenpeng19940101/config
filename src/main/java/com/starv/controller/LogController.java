package com.starv.controller;

import com.starv.entity.Log;
import com.starv.util.Result;
import com.starv.util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by m1590 on 2018/8/17.
 */
@RestController
@RequestMapping(value = "/log")
public class LogController {
    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final Logger logger= LoggerFactory.getLogger(LogController.class);

    @RequestMapping(value = "/list")
    public Result getList(@RequestParam(required = false)String user_id,
                          @RequestParam(required = false)String start_time,
                          @RequestParam(required = false)String end_time,
                          @RequestParam(required = false)String model_name,
                          @RequestParam(required = false)String content
    ){
        try {
            StringBuilder sql_count=new StringBuilder();
            sql_count.append("select count(1) from system_log");
            if (user_id!=null && user_id.length()>0){
                sql_count.append(" and user_id="+user_id);
            }
            if (model_name!=null && model_name.length()>0){
                sql_count.append(" and model_name like '%"+model_name+"%' ");
            }
            if (content!=null && content.length()>0){
                sql_count.append(" and content like '%"+content+"%' ");
            }
            if (start_time!=null && start_time.length()>0){
                sql_count.append(" and create_time >= "+start_time);
            }
            if (end_time!=null && end_time.length()>0){
                sql_count.append(" and create_time <= "+end_time);
            }
            sql_count.append(" order by  create_time desc ");
            long count = jdbcTemplate.queryForObject(sql_count.toString(), Long.class);
            if (count<1){
                return ResultUtil.SUCCESS("");
            }

            StringBuilder sql = new StringBuilder();
            sql.append("select * from system_log where 1=1");
            if (user_id!=null && user_id.length()>0){
                sql.append(" and user_id="+user_id);
            }
            if (model_name!=null && model_name.length()>0){
                sql.append(" and model_name like '%"+model_name+"%' ");
            }
            if (content!=null && content.length()>0){
                sql.append(" and content like '%"+content+"%' ");
            }
            if (start_time!=null && start_time.length()>0){
                sql.append(" and create_time >= "+start_time);
            }
            if (end_time!=null && end_time.length()>0){
                sql.append(" and create_time <= "+end_time);
            }
            sql.append(" order by  create_time desc");

            List<Log> logList = jdbcTemplate.query(sql.toString(), (resultSet, i) -> {
                Log log = new Log();
                log.setUser_id(resultSet.getInt("user_id"));
                log.setModel_name(resultSet.getString("model_name"));
                log.setContent(resultSet.getString("content"));
                log.setCreate_time(resultSet.getString("create_time"));

                return log;
            });

            return ResultUtil.SUCCESS(logList);

        } catch (Exception e) {
            logger.error("获取日志列表失败", e);
            return ResultUtil.ERROR("获取日志列表失败");
        }

    }

    @RequestMapping(value = "/add")
    public Result add( Log log){

        try {
            StringBuilder sql=new StringBuilder();
            sql.append("insert into system_log (user_id,model_name,content,create_time) values (?,?,?,now())");
            jdbcTemplate.update(sql.toString(),log.getUser_id(),log.getModel_name(),log.getContent());
            return ResultUtil.SUCCESS("日志添加成功");
        }catch (Exception e){
            logger.error("日志添加失败",e);
            return ResultUtil.ERROR("日志添加失败");
        }
    }

    @RequestMapping(value = "/delete")
    @Transactional(rollbackFor = Exception.class)
    public void  delete(){
        try {
            jdbcTemplate.update("DELETE FROM system_log WHERE 1=1");
        }catch (Exception e){
            logger.error("日志删除失败",e);
        }
    }
}
