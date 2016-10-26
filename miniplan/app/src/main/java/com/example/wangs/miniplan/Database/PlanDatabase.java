package com.example.wangs.miniplan.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by renjie on 2016/1/16.
 */
public class PlanDatabase extends SQLiteOpenHelper {
    //数据库名称
    private static final String DB_NAME = "PlanText.db";
    //表名
    private static final String TBL_NAME = "PlanTable1";
    private static final String TBL_NAME1 = "PlanTable2";

    //用于存储制定的计划
    public static final String CREATE_DIARY1="create table PlanTable1("
            +"_id integer primary key autoincrement,"
            +"planTitle text,"
            +"planTime text,"
            +"alarm text,"
            +"alarmTime text,"
            +"isDone text)";
    //用于存储每天完成的计划
    public static final String CREATE_DIARY2 = "create table PlanTable2("
            +"_id integer primary key autoincrement,"
            +"planedName text,"
            +"planTime text,"
            +"planedTime int,"
            +"planedDate text,"
            +"planingTime text,"
            +"planedMood text,"
            +"planedPercentage int)";
    //用于存储所有完成的计划
    public static final String CREATE_DIARY3 = "create table PlanTable3("
            +"_id integer primary key autoincrement,"
            +"planedName text,"
            +"planTime text,"
            +"planedTime int,"
            +"planedDate text,"
            +"planingTime text,"
            +"planedMood text,"
            +"planedPercentage int)";

    private SQLiteDatabase db;

    public PlanDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public PlanDatabase(Context context){
        super(context, DB_NAME, null, 2);
    }

    /**
     * 创建表
     * @param db SQLiteDatabase的实例
     */
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        db.execSQL(CREATE_DIARY1);
        db.execSQL(CREATE_DIARY2);
        db.execSQL(CREATE_DIARY3);
    }
    /**
     * 插入方法
     * @param values 插入的内容
     */
    public void insert(ContentValues values,String tableName){
        //获得SQLiteDatabase实例
        SQLiteDatabase db = getWritableDatabase();
        //插入
        db.insert(tableName,null,values);
        //关闭数据库
        db.close();
    }

    /**
     * 删除指定数据
     * @param id
     */
    public void del(int id,String tableName){
        if(db==null){
            db = getWritableDatabase();
        }
        //删除操作
        db.delete(tableName, "_id=?", new String[]{String.valueOf(id)});
    }
    public void update(ContentValues value, int id,String tableName){
        if(db == null){
            db = getWritableDatabase();
        }
        db.update(tableName,value,"_id=?",new String[]{String.valueOf(id)});
    }

    /**
     * 查询数据库
     * @return 返回Cursor
     */
    public Cursor query(String tableName){
        //获得SQLiteDatabase实例
        SQLiteDatabase db = getWritableDatabase();
        //查询获得Cursor
        Cursor c = db.query(tableName,null,null,null,null,null,null);
        return c;
    }

    public  void close(){
        if(db!=null){
            db.close();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}
