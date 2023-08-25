package com.example.google_map_to_track_user;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class DBhelper extends SQLiteOpenHelper {
    private  static final String DATABASE_NAME = "EmpLocationDb";
    private static final int DATABASE_VERSION = 3;
    private static final String TABLE_EMP_LOC = "tblEmplocation";
    private static final String TABLE_SETTINGS = "tblSettings";

    Context context;


    public DBhelper( Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_EMP_LOC+
                "(Id INTEGER PRIMARY KEY   AUTOINCREMENT,UID VARCHAR(250),RouteCode VARCHAR(250),UserCode VARCHAR(250),Latitude DOUBLE,Longitude DOUBLE,CurrentTime DATATIME,Attribute1 VARCHAR(250),\n" +
                "Attribute2 VARCHAR(250),\n" +
                "Attribute3 VARCHAR(250),\n" +
                "Attribute4 VARCHAR(250),\n" +
                "Attribute5 VARCHAR(250),\n" +
                "Status INT)");
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_SETTINGS+
                "(Id BIGINT NOT NULL,UID VARCHAR(250),Type VARCHAR(50),Name VARCHAR(250),Value VARCHAR(250),Datatype VARCHAR(50),ISEditable INT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("drop table if exists "+ TABLE_EMP_LOC);
        sqLiteDatabase.execSQL("drop table if exists "+TABLE_SETTINGS);
        onCreate(sqLiteDatabase);
    }

    public void insertData(double latitude, double longitude) {
        SQLiteDatabase DB=this.getWritableDatabase();
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = currentTime.format(calendar.getTime());

        UUID id=UUID.randomUUID();
        String uid=id.toString();

        ContentValues cv=new ContentValues();
        cv.put("UID",uid);
        cv.put("RouteCode","R1");
        cv.put("UserCode",108);
        cv.put("Latitude",latitude);
        cv.put("Longitude",longitude);
        cv.put("CurrentTime",date);
        cv.put("Status",0);


        DB.insert("tblEmplocation",null,cv);

        //Toast.makeText(context, "data added", Toast.LENGTH_SHORT).show();

        DB.close();
        Log.d("","lat"+latitude+" log"+longitude+" time"+date);
    }
    public ArrayList<LatLng> getLatLong(String starttime, String endtime){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ArrayList<LatLng> latLngslist = new ArrayList<>();
        Cursor cursor =sqLiteDatabase.rawQuery("SELECT Latitude,Longitude FROM " + TABLE_EMP_LOC +" WHERE CurrentTime BETWEEN '" +starttime+"' AND '"+endtime+"'",null);
        while (cursor.moveToNext()){
            latLngslist.add(new LatLng(cursor.getDouble(0),cursor.getDouble(1)));
            Log.d("cursor", "getLatLong: "+cursor.getDouble(0)+" "+cursor.getDouble(1));
        }
        sqLiteDatabase.close();
        return latLngslist;
    }
    /*public ArrayList<LatLng> getLatLong(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ArrayList<LatLng> latLngslist = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT Latitude,Longitude FROM "+ TABLE_EMP_LOC,null);
       // Cursor cursor =sqLiteDatabase.rawQuery("SELECT Latitude,Longitude FROM " + TABLE_EMP_LOC ,null);
        while (cursor.moveToNext()){
            latLngslist.add(new LatLng(cursor.getDouble(0),cursor.getDouble(1)));

    }
    return latLngslist;
        }*/
    public long getSettings() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("select Value FROM " + TABLE_SETTINGS, null);
        if (cursor.moveToNext()) {
            long sec = Long.parseLong(cursor.getString(0));
            Log.d("timeeeeeeeee", "getSettings: "+sec);
            sec=sec*1000;
            return sec;
        } else {
            return 5000;
        }

    }
    public Cursor PostDatalist(){
        int id = 0;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_EMP_LOC +" WHERE Status ='"+id+"'"+" LIMIT 100",null);
       /* if (!cursor.equals(null)) {
            Log.d("cursorsize", "PostDatalist: " + cursor.getCount());
        }*/
        // sqLiteDatabase.close();
        return cursor;
    }
    //posted data to server list status changing 1
    public boolean PostedDatatoserver(ArrayList<LatLagDO>statuschangelist){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long result=0;
        for (LatLagDO latLagDO : statuschangelist) {
            Log.d("PostedDatatoserver", "PostedDatatoserver: "+latLagDO.Id+Thread.currentThread());
            ContentValues cv = new ContentValues();
            int id = latLagDO.Id;
            cv.put("Status", 1);
            sqLiteDatabase.update(TABLE_EMP_LOC, cv, "Id=" + id, null);
        }
        if (result==-1){

            return false;
        }else {
            return true;
        }

    }
    public int TrackSqliteData(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        int id = 0;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_EMP_LOC +" WHERE Status ='"+id+"'",null);
        return cursor.getCount();
    }
    public int TrackserverData(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        int id = 1;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_EMP_LOC +" WHERE Status ='"+id+"'",null);
        return cursor.getCount();
    }
    //last 24 hours data deliting
    public void Deleteposted(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT CurrentTime FROM  tblEmplocation ORDER BY CurrentTime ASC LIMIT 1",null);
        cursor.moveToNext();
        String datetimes= cursor.getString(0);

        String hours24 = "+24 hour";
        int id = 1;
        //sqLiteDatabase.delete(TABLE_EMP_LOC,"WHERE CurrentTime BETWEEN '"+datetimes+"'AND datetime('"+datetimes+"', '+24 hour') and Status =1",null);

        Cursor cursor1=sqLiteDatabase.rawQuery("SELECT * FROM tblEmplocation WHERE CurrentTime BETWEEN '"+datetimes+"'AND datetime('"+datetimes+"', '"+hours24+"') and Status =1",null);
        if (cursor.getCount()>0) {
            //sqLiteDatabase.rawQuery("DELETE FROM tblEmplocation WHERE CurrentTime BETWEEN '"+datetimes+"'AND datetime('"+datetimes+"', '"+hours24+"') and Status =1",null);
        }else {

        }
        //Log.d("datetimes", "Deleteposted: "+datetimes+"  "+cursor1.getCount());
    }

}
