package com.jpr.sqlitedemo.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * 类描述：Sqlite建表类
 * 作者：jiaopeirong on 2018/6/3 16:24.
 * 邮箱：chinajpr@163.com
 */
public class PersionDBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "person.db";
    public static final String FORMNAME = "person";
    private static final int DBVERSIOIN = 1;

    /**
     * DBNAME 数据库的名字
     * factory 游标工厂
     * DBVERSION 数据库版本号（必须大于1）
     *
     * @param context
     */
    public PersionDBHelper(Context context) {
        super(context, DBNAME, null, DBVERSIOIN);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        //在这里创建表，不会出现重复创建表的错误
        //创建数据库表
        String sql = "create table person (_id integer primary key autoincrement,name varchar(20),sex char(2),nickname varchar(20))";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
