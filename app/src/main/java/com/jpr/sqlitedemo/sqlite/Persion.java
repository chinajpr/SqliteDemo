package com.jpr.sqlitedemo.sqlite;

/**
 * 类描述：Sqlite实体类
 * 作者：jiaopeirong on 2018/12/8 12:51
 * 邮箱：chinajpr@163.com
 */
public class Persion {
    private String name;
    private String sex;
    private String nickname;

    public Persion(String name, String sex, String nickname) {
        this.name = name;
        this.sex = sex;
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
