package com.jpr.sqlitedemo.orm.dao;

import java.util.List;

/**
 * 类描述：
 * 作者：jiaopeirong on 2018/11/27 22:56
 * 邮箱：chinajpr@163.com
 */
public interface IBaseDao<T> {
    /**
     * 插入数据
     *
     * @param entity
     * @return
     */
    Long insert(T entity);

    /**
     * 更改数据
     * @param entity
     * @param where
     * @return
     */
    Long update(T entity,T where);

    /**
     * 删除
     * @param where
     * @return
     */
    int delete(T where);

    /**
     * 查询
     * @param where
     * @return
     */
    List<T> query(T where);

    List<T> query(T where,String orderBy,Integer startIndex,Integer i);
}
