package com.jpr.sqlitedemo.orm.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.jpr.sqlitedemo.orm.annotion.DbFiled;
import com.jpr.sqlitedemo.orm.annotion.DbTable;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 类描述：Dao的父类
 * 1.维护表名和实例属性的映射
 * 2.将实例属性转成Map的形式
 * 3.将Map转换成ContentValues
 * 4.进行增删改查操作
 * 作者：jiaopeirong on 2018/11/27 22:56
 * 邮箱：chinajpr@163.com
 */
public abstract class BaseDao<T> implements IBaseDao<T> {
    private SQLiteDatabase database;
    //保证实例化一次
    private boolean isInit = false;
    //持有操作数据库表对应的类型
    private Class<T> entityClass;
    //维护表名与成员变量
    private HashMap<String, Field> cacheMap;
    //表名
    private String tableName;

    /**
     * 将map转换成ContentValue
     *
     * @param map
     * @return
     */
    private ContentValues getContentValues(Map<String, String> map) {
        ContentValues contentValues = new ContentValues();
        Set keys = map.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = map.get(key);
            if (value != null) {
                contentValues.put(key, value);
            }
        }
        return contentValues;
    }

    /**
     * 将对象转成Map的形式
     *
     * @param entity
     * @return
     */
    private Map<String, String> getValues(T entity) {
        HashMap<String, String> result = new HashMap<>();
        Iterator<Field> filedsIterator = cacheMap.values().iterator();
        //循环遍历映射表
        while (filedsIterator.hasNext()) {
            Field colmunToField = filedsIterator.next();
            String cacheKey = null;
            String cacheValue = null;
            if (colmunToField.getAnnotation(DbFiled.class) != null) {
                cacheKey = colmunToField.getAnnotation(DbFiled.class).value();
            } else {
                cacheKey = colmunToField.getName();
            }
            try {
                if (null == colmunToField.get(entity).toString()) {
                    continue;
                }
                cacheValue = colmunToField.get(entity).toString();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            result.put(cacheKey, cacheValue);
        }
        return result;
    }


    /**
     * 实例化
     *
     * @param entity
     * @param sqLiteDatabase
     * @return
     */
    public synchronized boolean init(Class<T> entity, SQLiteDatabase sqLiteDatabase) {
        if (!isInit) {
            this.entityClass = entity;
            database = sqLiteDatabase;
            if (entity.getAnnotation(DbTable.class) == null) {
                tableName = entity.getClass().getSimpleName();
            } else {
                tableName = entity.getAnnotation(DbTable.class).value();
            }

            if (!database.isOpen()) {
                return false;
            }

            //创建表
            if (!TextUtils.isEmpty(createTable())) {
                database.execSQL(createTable());
            }
            cacheMap = new HashMap<>();
            initCacheMap();

            isInit = true;
        }
        return isInit;
    }

    /**
     * 1.维护映射关系（表名和bean属性的映射关系），缓存
     */
    private void initCacheMap() {
        String sql = "select * from " + this.tableName + " limit 1 , 0";
        Cursor cursor = null;
        try {
            cursor = database.rawQuery(sql, null);
            String[] columnNames = cursor.getColumnNames();
            //拿到Filed数组
            Field[] columnFields = entityClass.getFields();
            for (Field field : columnFields) {
                field.setAccessible(true);
            }
            //开始找对应关系
            for (String colmunName : columnNames) {
                //如果找到对应的Filed就赋值
                Field colmunField = null;
                for (Field field : columnFields) {
                    String fieldName = null;
                    if (field.getAnnotation(DbFiled.class) != null) {
                        fieldName = field.getAnnotation(DbFiled.class).value();
                    } else {
                        fieldName = field.getName();
                    }
                    //如果表的列名 等于了 成员变量的注解名
                    if (colmunName.equals(fieldName)) {
                        colmunField = field;
                        break;
                    }
                }
                //找到了对应关系
                if (colmunField != null) {
                    cacheMap.put(colmunName, colmunField);
                }
            }
        } catch (Exception e) {

        } finally {
            cursor.close();
        }


    }

    /**
     * 创建表
     *
     * @return
     */
    protected abstract String createTable();

    @Override
    public Long insert(T entity) {
        Map<String, String> map = getValues(entity);
        ContentValues values = getContentValues(map);
        Long result = database.insert(tableName, null, values);
        return result;
    }

    @Override
    public int delete(Object where) {
        return 0;
    }

    @Override
    public List query(Object where) {
        return null;
    }

    @Override
    public List query(Object where, String orderBy, Integer startIndex, Integer i) {
        return null;
    }

    @Override
    public Long update(T entity, T where) {
        return null;
    }
}
