package com.jpr.sqlitedemo.orm;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.jpr.sqlitedemo.orm.dao.BaseDao;

import java.io.File;

/**
 * 类描述：BaseDao的工厂类
 * 作者：jiaopeirong on 2018/11/27 22:58
 * 邮箱：chinajpr@163.com
 */
public class BaseDaoFactory {
    //数据库路径
    private String sqilteDatbasePath;
    private SQLiteDatabase sqLiteDatabase;
    private static BaseDaoFactory baseDaoFactory = new BaseDaoFactory();

    public static BaseDaoFactory getInstance(){
        return baseDaoFactory;
    }

    private BaseDaoFactory() {
//        sqilteDatbasePath = Environment.getDataDirectory().getAbsolutePath() +"/user.db";
        sqilteDatbasePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator+"User.db";
        openDatabase();
    }

    private void openDatabase() {
        this.sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(sqilteDatbasePath, null);
    }


    public synchronized <T extends BaseDao<M>, M> T getDataHelper(Class<T> clazz, Class<M> entityClass) {
        BaseDao baseDao = null;
        try {
            baseDao = clazz.newInstance();
            baseDao.init(entityClass, sqLiteDatabase);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (T) baseDao;
    }
}
