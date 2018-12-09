package com.jpr.sqlitedemo;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jpr.sqlitedemo.orm.BaseDaoFactory;
import com.jpr.sqlitedemo.orm.dao.IBaseDao;
import com.jpr.sqlitedemo.orm.dao.UserDao;
import com.jpr.sqlitedemo.sqlite.Persion;
import com.jpr.sqlitedemo.sqlite.PersionDBHelper;
import com.jpr.sqlitedemo.sqlite.PersionDao;

/**
 * 1：Sqlite 提供的操作类：
 * SQLiteDatabase、SQLiteOpenHelper 用于创建数据库
 * Cursor用于表的查询
 * ContentValues用于数据插入
 * <p>
 * 2：操作Sqlite用到的类
 * 建表类
 * 操作类dao
 * 实体类bean
 * <p>
 * 3:操作数据库的方式
 * 增：sql语句方式db.execSQL();原生方式db.insert(),使用到ContentValues类;
 * 删：sql语句方式db.execSQL();原生方式db.delete();
 * 改：sql语句方式db.execSQL();原生方式db.update();使用到ContentValues类;
 * 查：sql语句方式db.rawQuery();原生方式db.query();两者的返回值均是Cursor对象.
 */
public class MainActivity extends AppCompatActivity {

    private IBaseDao<User> baseDao;
    private Persion persionBean = new Persion("原生", "男", "000000");
    private Persion persionSql = new Persion("sql语句", "女", "1111111");
    //读写权限
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;
    //可以用统一的Dao,也可以重新new
    private PersionDao persionSqlDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            } else {
                initORM();
            }
        } else {
            initORM();
        }

        persionSqlDao = new PersionDao(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                Log.i("MainActivity", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
                initORM();
            }
        }
    }

    private void initORM() {
        baseDao = BaseDaoFactory.getInstance().getDataHelper(UserDao.class, User.class);
    }


    /*************************原生数据库start***********************************/
    /**
     * 增
     *
     * @param view
     */
    public void insert(View view) {
        PersionDao persionDao = new PersionDao(this);
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", persionBean.getName());
        contentValues.put("sex", persionBean.getSex());
        contentValues.put("nickname", persionBean.getNickname());
        long l = persionDao.insertValue(PersionDBHelper.FORMNAME, contentValues);
        Toast.makeText(this, l + "", Toast.LENGTH_SHORT).show();
    }

    /**
     * 删
     *
     * @param view
     */
    public void del(View view) {
        PersionDao persionDao = new PersionDao(this);
        int delete = persionDao.delete(PersionDBHelper.FORMNAME, "name=?", new String[]{persionBean.getName()});
        Toast.makeText(this, delete + "", Toast.LENGTH_SHORT).show();
    }


    /**
     * 改
     *
     * @param view
     */
    public void update(View view) {
        PersionDao persionDao = new PersionDao(this);
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", persionBean.getName() + "改名字啦");
        int update = persionDao.update(PersionDBHelper.FORMNAME, contentValues, "name=?", new String[]{persionBean.getName()});
        Toast.makeText(this, update + "", Toast.LENGTH_SHORT).show();
    }

    /**
     * 查
     *
     * @param view
     */
    public void query(View view) {
        PersionDao persionDao = new PersionDao(this);
        String sql = "select * from person where name=?";
        Cursor cursor = persionDao.query(sql, new String[]{persionBean.getName()});
        while (cursor.moveToNext()) {
            int _id = cursor.getInt(0);
            // 根据列名得到列号
            int nameIndex = cursor.getColumnIndex("name");
            // 根据列号得到列中的值
            String name = cursor.getString(nameIndex);
            String sex = cursor.getString(2);
            String nickname = cursor.getString(3);
            Log.i("TAG", "_id=" + _id + ",name=" + name + ",sex=" + sex
                    + ",nickname=" + nickname);
        }
    }


    /**
     * sql语句插入
     *
     * @param view
     */
    public void inserSql(View view) {
        String sql = "insert into " + PersionDBHelper.FORMNAME + "(name,sex,nickname)values(?,?,?)";
        Object arr[] = new Object[]{persionSql.getName(), persionSql.getSex(), persionSql.getNickname()};
//        PersionDao persionDao = new PersionDao(this);
        persionSqlDao.sqlInsert(sql, arr);
    }

    /**
     * sql语句删除
     *
     * @param view
     */
    public void delSql(View view) {
        String sql = "delete from " + PersionDBHelper.FORMNAME + " where name=?";
        Object arr[] = {persionSql.getName()};
        persionSqlDao.sqlDel(sql, arr);
    }

    /**
     * sql语句修改
     *
     * @param view
     */
    public void updateSql(View view) {
        String sql = "update " + PersionDBHelper.FORMNAME + " set name=?,sex=? where name=?";
        Object arr[] = {persionSql.getName() + "修改啦", persionSql.getSex() + "修改啦", persionSql.getName()};
        persionSqlDao.sqlUpdate(sql, arr);
    }

    /**
     * sql语句查询
     *
     * @param view
     */
    public void querySql(View view) {
    }


    /***************************原生数据库end***************************************/
    /***************************ORM数据库START***************************************/

    /**
     * orm插入
     *
     * @param view
     */
    public void ormAdd(View view) {
        User user = new User("teacher", "123");
        baseDao.insert(user);
    }

    /**
     * orm删除
     *
     * @param view
     */
    public void ormDel(View view) {

    }

    /**
     * orm修改
     *
     * @param view
     */
    public void ormUpdate(View view) {
    }

    /**
     * orm查询
     *
     * @param view
     */
    public void ormQuery(View view) {
    }

    /***************************ORM数据库END***************************************/

}
