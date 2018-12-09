package com.jpr.sqlitedemo;

import com.jpr.sqlitedemo.orm.annotion.DbFiled;
import com.jpr.sqlitedemo.orm.annotion.DbTable;

/**
 * 类描述：
 * 作者：jiaopeirong on 2018/11/27 22:55
 * 邮箱：chinajpr@163.com
 */
@DbTable("tb_user")
public class User {

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    @DbFiled("name")
    public String name;
    @DbFiled("password")
    public String password;

}
