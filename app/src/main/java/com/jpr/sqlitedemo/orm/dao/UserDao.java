package com.jpr.sqlitedemo.orm.dao;

import java.util.List;

/**
 * 类描述：
 * 作者：jiaopeirong on 2018/11/27 23:27
 * 邮箱：chinajpr@163.com
 */
public class UserDao extends BaseDao {

    @Override
    protected String createTable() {
        return "create table if not exists tb_user(name varchar(20),password varchar(10))";
    }


}
