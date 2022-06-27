package com.tianxin.Test;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tianxin.app.DemoApplication;
import com.tencent.opensource.model.item;
import com.tianxin.Util.log;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库创建操作类
 */
public class MyOpenhelper extends SQLiteOpenHelper {
    public static MyOpenhelper smyOpenhelper;
    public static final String search = "search";

    public synchronized static MyOpenhelper getOpenhelper() {
        if (smyOpenhelper == null) {
            smyOpenhelper = new MyOpenhelper(DemoApplication.instance());
        }
        return smyOpenhelper;
    }

    public MyOpenhelper(Context context) {
        super(context, "paixide.db", null, 1);
    }

    public MyOpenhelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //个人资料
        String member = "create table member(id integer primary key autoincrement,username varchar(32),password  varchar(32),rpassword varchar(32),mobile varchar(32),truename varchar(32),parent varchar(32),sex int,jinbi decimal(12,4),status int,msg varchar(32),wxpic text,dadetime varchar(32))";
        //广告内容
        String Adcontent = "create table Adcontent(id integer primary key autoincrement,title varchar(100),banner varchar(200),Copyright text,Description text,Author text,dadetime varchar(32))";
        //我的视频
        String Myvideo = "create table myvideo(id integer primary key autoincrement,userid int(12),title varchar(100),pic text,url text,msg varchar(100),type int,dadetime varchar(32))";
        //我的相册
        String album = "create table album(id integer primary key autoincrement,userid int(12),title varchar(100),pic text,url text,msg varchar(100),type int,dadetime varchar(32))";
        //我的收藏
        String Collection = "create table collection(id integer primary key autoincrement,userid int(12),title text,Description text,pic varchar(200),url varchar(200),Author varchar(200),dadetime varchar(32))";
        //我的浏览
        String browse = "create table browse(id integer primary key autoincrement,userid int,title text,keywords text,Copyright text,Description text,Author text,dadetime varchar(32))";
        //收货地址
        String address = "create table address(id integer primary key autoincrement,userid int(12),username varchar(32),phone varchar(11),address text,ip varchar(32),datetime varchar(32))";
        //创建搜索记录
        String searchs = "create table search(id integer primary key autoincrement, title text,type int)";

        //执行创建数居表
        db.execSQL(member);
        db.execSQL(Adcontent);
        db.execSQL(Myvideo);
        db.execSQL(album);
        db.execSQL(Collection);
        db.execSQL(browse);
        db.execSQL(address);
        db.execSQL(searchs);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.print("数据库升级了!!!");
    }

    /**
     * 插入数据到旨定表中1
     */
    public void insert1(SQLiteDatabase db) {
        String stu_sql = "insert into paixide(title,keywords,Copyright,Description,Author) values('xiaoming','01005','xiaoming','01005','xiaoming')";
        db.execSQL(stu_sql);
    }

    /**
     * 插入数据到旨定表中2
     */
    public void insert(SQLiteDatabase db) {
        ContentValues cValue = new ContentValues();
        cValue.put("title", "习近平发表演讲 习近平夫妇会见马克龙夫妇");
        cValue.put("keywords", "习近平发表演讲 习近平夫妇会见马克龙夫妇");
        cValue.put("Copyright", "习近平发表演讲 习近平夫妇会见马克龙夫妇");
        cValue.put("Description", "习近平发表演讲 习近平夫妇会见马克龙夫妇");
        cValue.put("Author", "习近平发表演讲 习近平夫妇会见马克龙夫妇");
        db.insert("paixide", null, cValue);
    }

    /**
     * 插入数据到旨定表中3
     *
     * @param table
     * @param cValue
     */
    public void insert(String table, ContentValues cValue) {
        SQLiteDatabase db = getReadableDatabase();
        db.insert(table, null, cValue);
        db.close();
    }

    public void update1(SQLiteDatabase db) {
        //实例化内容值
        ContentValues values = new ContentValues();
        //在values中添加内容
        values.put("title", "101003");
        //修改条件
        String whereClause = "id=?";
        //修改添加参数
        String[] whereArgs = new String[]{"1"};
        //修改
        db.update("paixide", values, whereClause, whereArgs);

    }

    public void update2(SQLiteDatabase db) {
        //修改SQL语句
        String sql = "update paixide set title = '666666' where id = 2";
        //执行SQL
        db.execSQL(sql);
    }

    public void delete1(SQLiteDatabase db) {
        //删除条件
        String whereClause = "id=?";
        //删除条件参数
        String[] whereArgs = {String.valueOf(1)};
        //执行删除
        db.delete("paixide", whereClause, whereArgs);
    }

    public void delete(SQLiteDatabase db) {
        //删除SQL语句
        String sql = "delete from paixide where id = 2";
        //执行SQL语句
        db.execSQL(sql);
    }

    /**
     * 删除所有数据ALL
     *
     * @param table
     */
    public void delete(String table) {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL(String.format("delete from %s", table));
        db.close();
    }

    /**
     * 删除数据内容
     *
     * @param table
     * @param type
     */
    public void delete(String table, int type) {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("delete from " + table + " where type=" + type);
    }

    /**
     * 删除数据内容
     *
     * @param table
     * @param title
     */
    public void delete(String table, String title) {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL(String.format("delete from %s where title='%s'", table, title));
    }


    /**
     * 查询数据
     *
     * @param db
     */
    public void Query(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("select * from paixide", null);
        while (cursor.moveToNext()) {
            try {
                String string0 = cursor.getString(0);
                String string1 = cursor.getString(1);
                String string2 = cursor.getString(2);
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex("title"));
                log.d(string0 + "|" + string1 + "|" + string2 + "|" + title);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        db.close();


    }

    public List<item> Query(String table) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + table + "", null);
        List<item> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String uid = cursor.getString(cursor.getColumnIndex("uid"));
            @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex("title"));
            @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("description"));
            @SuppressLint("Range") String show_num = cursor.getString(cursor.getColumnIndex("show_num"));
            @SuppressLint("Range") String type = cursor.getString(cursor.getColumnIndex("type"));
            @SuppressLint("Range") String videos = cursor.getString(cursor.getColumnIndex("videos"));


        }
        cursor.close();
        db.close();
        return list;
    }

    /**
     * 查询指定表数据
     *
     * @param table
     * @param type
     * @return
     */
    public List<String> Query(String table, int type) {
        SQLiteDatabase db = getReadableDatabase();
        List<String> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(String.format("select * from %s where type=%s", table, type), null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex("title"));
            list.add(title);
        }
        cursor.close();
        db.close();
        return list;
    }

}
