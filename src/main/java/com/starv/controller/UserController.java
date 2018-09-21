package com.starv.controller;

import com.starv.entity.User;
import com.starv.util.Result;
import com.starv.util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by m1590 on 2018/8/14.
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    JdbcTemplate jdbcTemplate;


    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/getList")
    public Result getList(@RequestBody User user) {
        try {
            StringBuilder sql_count=new StringBuilder();
            sql_count.append("select count(1) from system_user where 1=1 ");
            if (user.getUser_id()!=null && user.getUser_id()>0){
                sql_count.append(" and user_id="+user.getUser_id());
            }
            logger.info("user_name=="+user.getUser_name());
            if (user.getUser_name()!=null && user.getUser_name().length()>0){
                sql_count.append(" and user_name like '%"+user.getUser_name()+"%' ");
            }
            long count = jdbcTemplate.queryForObject(sql_count.toString(), Long.class);
            if (count<1){
                logger.info("sql_count==  "+sql_count.toString());
                return ResultUtil.SUCCESS("");
            }

            StringBuilder sql = new StringBuilder();
            sql.append("select * from system_user where 1=1");
            if (user.getUser_id()!=null && user.getUser_id()>0){
                sql.append(" and user_id="+user.getUser_id());
            }
            if (user.getUser_name()!=null && user.getUser_name().length()>0){
                sql.append(" and user_name like '%"+user.getUser_name()+"%' ");
            }

            List<User> userList = jdbcTemplate.query(sql.toString(), (resultSet, i) -> {
                User  u= new User();
                u.setUser_id(resultSet.getInt("user_id"));
                u.setCreate_time(resultSet.getString("create_time"));
                u.setPassword(resultSet.getString("password"));
                u.setUser_name(resultSet.getString("user_name"));

                return u;
            });

            return ResultUtil.SUCCESS(userList);

        } catch (Exception e) {
            logger.error("获取用户列表失败", e);
            return ResultUtil.ERROR("获取用户列表失败");
        }

    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @Transactional
    public Result add(@RequestBody@Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultUtil.ERROR(bindingResult.getFieldError().getDefaultMessage());
        }

        try {
            StringBuilder userCount_sql=new StringBuilder();
            userCount_sql.append("select count(1) from system_user where user_name=? ");
            Integer userCount = jdbcTemplate.queryForObject(userCount_sql.toString(), Integer.class, user.getUser_name());
            if (userCount>0){
                return ResultUtil.ERROR("用户名已经存在");
            }

            StringBuilder sql = new StringBuilder();
            sql.append("insert into system_user (user_name,password,create_time) values (?,?,now())");
            jdbcTemplate.update(sql.toString(), user.getUser_name(), user.getPassword());
            return ResultUtil.SUCCESS("用户添加成功");
        } catch (Exception e) {
            logger.error("用户添加失败", e);
            return ResultUtil.ERROR("用户添加失败");
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @Transactional
    public Result update(@RequestBody@Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultUtil.ERROR(bindingResult.getFieldError().getDefaultMessage());
        }

        try {
            if (user.getUser_id() != null && user.getUser_id() > 0) {
                StringBuilder sql = new StringBuilder();
                sql.append(" update system_user set  user_name = ?,password = ? where user_id = ?");
                jdbcTemplate.update(sql.toString(), user.getUser_name(), user.getPassword(), user.getUser_id());
                return ResultUtil.SUCCESS("用户更新成功");
            } else {
                return ResultUtil.ERROR("用户ID必传");
            }
        } catch (Exception e) {
            logger.error("用户更新失败", e);
            return ResultUtil.ERROR("用户更新失败");
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @Transactional
    public Result delete(@RequestBody User user) {

        try {
            if (user.getUser_id() != null && user.getUser_id() > 0) {
                logger.info("user_id== "+user.getUser_id());
                StringBuilder sql = new StringBuilder();
                sql.append("delete from system_user  where user_id = ?");
                jdbcTemplate.update(sql.toString(), user.getUser_id());
                return ResultUtil.SUCCESS("用户删除成功");
            } else {
                return ResultUtil.ERROR("用户ID异常");
            }
        } catch (Exception e) {
            logger.error("用户删除失败", e);
            return ResultUtil.ERROR("用户删除失败");
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(@RequestBody@Valid User user,BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return ResultUtil.ERROR(bindingResult.getFieldError().getDefaultMessage());
        }

        try {
            StringBuilder sql=new StringBuilder();
            sql.append("select user_id from system_user where user_name=? and password=?");
            Integer user_id = jdbcTemplate.queryForObject(sql.toString(), Integer.class,
                    user.getUser_name(), user.getPassword());
            if (user_id!=null && user_id>0){
                HttpSession session = request.getSession();
                session.setAttribute("user_id",user_id);
                session.setAttribute("user_name",user.getUser_name());
            }
            return ResultUtil.SUCCESS("登陆成功");

        } catch (Exception e) {
            logger.error("登陆失败",e);
            return ResultUtil.ERROR("登陆失败");
        }
    }

    @RequestMapping(value = "/exitLogin")
    public Result exitLogin( HttpServletRequest request) {

        try {
            HttpSession session = request.getSession();
            session.invalidate();
            logger.info("------退出登陆-------");
            return  ResultUtil.SUCCESS("退出登陆");
        } catch (Exception e) {
            logger.error("退出登陆失败", e);
            return  ResultUtil.ERROR("退出登陆失败");
        }
    }




}
