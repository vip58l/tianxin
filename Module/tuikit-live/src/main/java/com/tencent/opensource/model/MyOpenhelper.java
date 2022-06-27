package com.tencent.opensource.model;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tencent.qcloud.tim.tuikit.live.TUIKitLive;
import com.tencent.qcloud.tim.tuikit.live.base.Constants;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.annotation.Retention;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库创建操作类
 */
public class MyOpenhelper extends SQLiteOpenHelper {


    private String TAG = MyOpenhelper.class.getSimpleName();
    public static MyOpenhelper smyOpenhelper;
    //记录短视频付费记录 方便下次无需再次付费
    public final static String videolist = "videolist";
    //聊天消息每人最多只能发6条消息然提示南要付费聊天
    public final static String paidchat = "paidchat";
    public static final String search = "search";
    public static final String cahtreply = "cahtreply";
    public int type = 1; //存视频文件
    private UserInfo userInfo;
    private int getPosition;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public static MyOpenhelper getOpenhelper() {
        if (smyOpenhelper == null) {
            smyOpenhelper = new MyOpenhelper(TUIKitLive.getAppContext());
        }
        return smyOpenhelper;
    }

    public MyOpenhelper(Context context) {
        super(context, "paixide.db", null, 1);
        userInfo = UserInfo.getInstance();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建据库表
        db.execSQL("create table if not exists videolist(_id integer primary key autoincrement,dept_id int(12),title text,type int,page int,dadetime varchar(32))");
        db.execSQL("create table if not exists paidchat(_id integer primary key autoincrement,dept_id int(12),title text,type int,page int,chat int,dadetime varchar(32))");

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

        //聊天记录
        String cahtreply = "create table cahtreply(id integer primary key autoincrement, title text,type int)";

        //执行创建数居表
        db.execSQL(member);
        db.execSQL(Adcontent);
        db.execSQL(Myvideo);
        db.execSQL(album);
        db.execSQL(Collection);
        db.execSQL(browse);
        db.execSQL(address);
        db.execSQL(searchs);
        db.execSQL(cahtreply);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.print("数据库升级了!!!");
    }

    public void insert(String sql) {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL(sql);
        db.close();
    }

    public void insert(String table, String json, int type, int page) {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL(String.format("insert into %s(title,type,page) values('%s',%s,%s)", table, json, type, page));
        db.close();
    }

    public void insert1(String table, String title, int id, int type) {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL(String.format("insert into %s(dept_id,title,type) values(%s,'%s',%s)", table, id, title, type));
        db.close();
    }

    public boolean Querys(String table, int id, int type) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(String.format("select * from %s where dept_id=%s and type=%s", table, id, type), null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0 ? true : false;
    }

    public String Querys(String table, int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(String.format("select * from %s where dept_id=%s", table, id), null);
        List<String> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex("title"));
            list.add(title);
        }
        cursor.close();
        db.close();
        return list.size() > 0 ? list.get(0) : "";

    }


    public void Update(String table, String title, int dept_id, int type) {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL(String.format("update %s set title='%s' where dept_id=%s and type=%s", table, title, dept_id, type));
        db.close();
    }


    /**
     * 插入数据到旨定表中2
     */
    public void insert2() {
        SQLiteDatabase db = getReadableDatabase();
        ContentValues cValue = new ContentValues();
        cValue.put("Author", "习近平发表演讲 习近平夫妇会见马克龙夫妇");
        db.insert("paixide", null, cValue);
        cValue.clear();
        db.close();

    }

    /**
     * 插入数据到旨定表中3
     *
     * @param table
     * @param cValue
     */
    public void insert3(String table, ContentValues cValue) {
        SQLiteDatabase db = getReadableDatabase();
        db.insert(table, null, cValue);
        db.close();
    }

    public void update1(String sql) {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL(sql);
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

    public void delete2(SQLiteDatabase db) {
        //删除SQL语句
        String sql = "delete from paixide where id = 2";
        //执行SQL语句
        db.execSQL(sql);
    }

    public void delete3(String table) {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL(String.format("delete from %s", table));
        db.close();
    }

    public void delete4(String table, int type) {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("delete from " + table + " where page=" + type);
        db.close();
    }

    public void delete5(String table, int id) {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL(String.format("delete from %s where dept_id=%s", table, id));
        db.close();
    }

    public void Query(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("select * from paixide", null);
        while (cursor.moveToNext()) {
            String string0 = cursor.getString(0);
            String string1 = cursor.getString(1);
            String string2 = cursor.getString(2);
            @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex("title"));

        }
        cursor.close();
        db.close();


    }

    public List<String> Query(String table, int id, String userid, int TYPE) {
        List<String> list = new ArrayList<>();
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(String.format("select * from %s where dept_id=%s and title=%s and type=%s", table, id, userid, TYPE), null);
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex("title"));
                list.add(title);
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String> Query(String table, String userid, int id, int TYPE) {
        List<String> list = new ArrayList<>();
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(String.format("select * from %s where dept_id=%s and title='%s' and type=%s", table, id, userid, TYPE), null);
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex("title"));
                list.add(title);
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Object> Querychat(String table, String userid, int TYPE) {
        List<Object> list = new ArrayList<>();
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(String.format("select * from %s where title='%s' and type=%s", table, userid, TYPE), null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    int page = cursor.getInt(cursor.getColumnIndex("page"));
                    int chats = cursor.getInt(cursor.getColumnIndex("chat"));
                    chat chat = new chat();
                    chat.setUserid(Integer.parseInt(title));
                    chat.setPage(page);
                    chat.setFree(chats);
                    list.add(chat);
                }
                cursor.close();
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String> Query(String table) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(String.format("select * from %s order by _id desc limit 0,1", table), null);
        List<String> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex("title"));
            list.add(title);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<String> QueryPage(String table, int page) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(String.format("select * from %s order by _id desc limit %s,5", table, page), null);
        List<String> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex("title"));
            getPosition++; //分页查询第一条开始查询 跟page 对应
            list.add(title);
        }
        cursor.close();
        db.close();
        return list;
    }

    public boolean Query(String table, int page) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(String.format("select * from %s where page=%s", table, page), null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0 ? true : false;
    }

    public String Query4(String table, int type, int page) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(String.format("select * from %s where type=%s and page=%s", table, type, page), null);
        List<String> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex("title"));
            list.add(title);
        }
        cursor.close();
        db.close();
        return list.size() > 0 ? list.get(0) : "";
    }

    private JSONArray getJSONArray(String videos) {
        try {
            return new JSONArray(videos);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 更新数据
     *
     * @param table
     * @param json
     * @param page
     */
    public void update5(String table, String json, int type, int page) {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL(String.format("update %s set title='%s' where type=%s and page=%s", table, json, type, page));
        db.close();
    }

    /**
     * 更新数据
     *
     * @param table
     * @param json
     * @param page
     */
    public void update6(String table, String json, int page) {
        SQLiteDatabase db = getReadableDatabase();
        //db.execSQL(String.format("update member set title ='%s' where page=&s", table, json, page));
        //db.close();

        //实例化内容值
        ContentValues values = new ContentValues();
        //在values中添加内容
        values.put("title", json);
        //修改条件
        String whereClause = "page=?";
        //修改条件参数
        String[] whereArgs = new String[]{"'" + page + "'"};
        //修改
        db.update(table, values, whereClause, whereArgs);
    }

    /**
     * 每次聊天前需要检测是否符合条件才允许发送消息
     *
     * @return
     */
    public boolean chatQuerychat() {
        if (userInfo.getVip() == Constants.TENCENT || userInfo.getSex().equals("2")) {
            if (userInfo.getState() == Constants.TENCENT3) {
                Log.d(TAG, "chatQuerychat: 被封号了");
                return true;
            }
            return false;
        }
        List<Object> querychat = Querychat(MyOpenhelper.paidchat, userInfo.getUserId(), 2);
        if (querychat.size() > 0) {
            chat chat = (chat) querychat.get(0);
            if (chat.getPage() >= chat.getFree()) {
                Log.d(TAG, "聊天次数已超过限制" + chat.getPage() + ">" + chat.getFree());
                return true;
            }
            //更新聊天消息内容
            insert(String.format("update %s set page=%s where title='%s' and type=%s", MyOpenhelper.paidchat, chat.getPage() + 1, userInfo.getUserId(), 2));
        }
        return false;
    }

    /**
     * 每次聊天前需要检测是否符合条件才允许发送消息
     *
     * @return
     */
    public boolean chatQuerychat2() {
        if (userInfo.getVip() == Constants.TENCENT || userInfo.getSex().equals("2")) {
            if (userInfo.getState() == Constants.TENCENT3) {
                Log.d(TAG, "chatQuerychat: 被封号了");
                return true;
            }
            return false;
        }

        List<Object> querychat = Querychat(MyOpenhelper.paidchat, userInfo.getUserId(), 2);
        if (querychat.size() > 0) {
            chat chat = (chat) querychat.get(0);
            if (chat.getPage() >= chat.getFree()) {
                Log.d(TAG, "聊天次数已超过限制" + chat.getPage() + ">" + chat.getFree());
                return true;
            }
        }
        return false;

    }


    /**
     * 插入聊天数据库
     *
     * @param getId
     * @param getFree
     */
    public void add(int getId, int getFree) {
        insert(String.format("insert into %s(dept_id,title,chat,type) values(%S,'%s',%s,%s)", MyOpenhelper.paidchat, getId, userInfo.getUserId(), getFree, 2));
    }

    /**
     * 更新聊天次数
     */
    public void upchat(int free) {
        update1(String.format("update %s set chat=%s where title=%s and type=%s", MyOpenhelper.paidchat, free, userInfo.getUserId(), 2));
    }

    /**
     * 创建聊天上限消息条数
     *
     * @param id
     * @return
     */
    public boolean check(int id) {
        List<String> query = Query(MyOpenhelper.paidchat, userInfo.getUserId(), id, 2);
        return query.size() == 0 ? false : true;
    }

    /**
     * 返回所有总条数
     *
     * @param cahtreply
     * @param type
     * @return
     */
    public int conuntQuery(String cahtreply, int type) {
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(String.format("select count(id) from %s where type=%s", cahtreply, type), null);
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();
            return count;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return 0;
    }
}
