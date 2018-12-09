package com.jpr.sqlitedemo.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import org.xml.sax.helpers.LocatorImpl;

/**
 * 类描述：Sqlite操作类
 * 作者：jiaopeirong on 2018/6/3 16:28
 * 邮箱：chinajpr@163.com
 */
public class PersionDao {
    private SQLiteDatabase db;

    public PersionDao(Context context) {
        //创建数据库和数据库表
        PersionDBHelper persionDBHelper = new PersionDBHelper(context);
        db = persionDBHelper.getWritableDatabase();

        //在这里创建表，会报重复创建表的错误,所以要添加if not exists
        String sql2 = "create table if not exists person2 (_id integer primary key autoincrement,name varchar(20),sex char(2),nickname varchar(20))";
        db.execSQL(sql2);
    }

    /*****************************原生语句操作数据库start************************************/
    /**
     * 插入语句
     *
     * @param table
     * @param values
     * @return
     */
    public long insertValue(String table, ContentValues values) {
        db.beginTransaction();
        long insert = 0;
        try {
            insert = db.insert(table, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {

        } finally {
            db.endTransaction();
            db.close();
        }
        return insert;
    }

    /**
     * 查询
     *
     * @param sql
     * @param args
     * @return
     */
    public Cursor query(String sql, String[] args) {
        return db.rawQuery(sql, args);
    }

    /**
     * 修改
     *
     * @param table
     * @param values
     * @param whereClause
     * @param whereArgs
     * @return 返回值：修改了几条符合条件的，就返回几
     */
    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        return db.update(table, values, whereClause, whereArgs);
    }

    /**
     * 删除数据
     *
     * @param table
     * @param whereClause
     * @param whereArgs
     * @return 返回值：删除了几条符合条件的，就返回几
     */
    public int delete(String table, String whereClause, String[] whereArgs) {
        return db.delete(table, whereClause, whereArgs);
    }

    /*****************************原生语句操作数据库end***********************************/
    /*****************************sql语句操作数据库start***********************************/
    public void sqlInsert(String sql, Object[] arr) {
        db.execSQL(sql, arr);
    }

    public void sqlDel(String sql, Object[] arr) {
        db.execSQL(sql, arr);
    }

    public void sqlUpdate(String sql, Object[] arr) {
        db.execSQL(sql, arr);
    }
    /*****************************sql语句操作数据库end***********************************/


}
