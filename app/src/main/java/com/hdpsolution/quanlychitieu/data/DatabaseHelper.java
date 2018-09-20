package com.hdpsolution.quanlychitieu.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.hdpsolution.quanlychitieu.comom.Commom;
import com.hdpsolution.quanlychitieu.model.ThuChi;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    // database
    private static final int DATABASE_VERSION = 19;
    private static final String DATABASE_NAME = "Thongke.db";
    // table name
    private static final String TBNAME = "thuchi"; //
    private static final String KEY_ID = "_Id";
    private static final String KEY_NOTE = "Note";
    private static final String KEY_NUMBER = "Number";
    private static final String KEY_DATE = "Date";
    private static final String TYPEMONEY = "Type";
    private static final String DANHMUC = "Danhmuc";
    private static final String TYPESPINER = "TypeSpiner";

    private SQLiteDatabase sqLiteDatabase;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE "+TBNAME+"(\n" +
                "\t"+KEY_ID+"\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t"+KEY_NOTE+"\tTEXT,\n" +
                "\t"+DANHMUC+"\tTEXT,\n" +
                "\t"+KEY_NUMBER+"\tTEXT,\n" +
                "\t"+KEY_DATE+"\tNUMERIC,\n" +
                "\t"+TYPEMONEY+"\tTEXT,\n" +
                "\t"+TYPESPINER+"\tTEXT\n" +
                ");";


        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TBNAME);
        onCreate(db);
    }

    //functions
    public boolean addMoney(ThuChi thuChi) {
        try {
            sqLiteDatabase = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_NOTE, thuChi.getNote());
            values.put(DANHMUC, thuChi.getDanhmuc());
            values.put(KEY_NUMBER, String.valueOf(thuChi.getNumber()));
            values.put(KEY_DATE, thuChi.getDate());
            values.put(TYPEMONEY, String.valueOf(thuChi.getType()));
            values.put(TYPESPINER, String.valueOf(thuChi.getTypeSpiner()));
            if (sqLiteDatabase.insert(TBNAME, null, values) != -1) {
                sqLiteDatabase.close();
                return true;
            }

        } catch (Exception e) {
            sqLiteDatabase.close();
            return false;
        }
        sqLiteDatabase.close();
        return false;
    }

    public boolean upDateMoney(ThuChi thuChi) {

        sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NOTE, thuChi.getNote());
        values.put(DANHMUC, thuChi.getDanhmuc());
        values.put(KEY_NUMBER, String.valueOf(thuChi.getNumber()));
        values.put(KEY_DATE, thuChi.getDate());
        values.put(TYPEMONEY, String.valueOf(thuChi.getType()));
        values.put(TYPESPINER, String.valueOf(thuChi.getTypeSpiner()));
        if ((sqLiteDatabase.update(TBNAME, values, KEY_ID + " = " + String.valueOf(thuChi.getId()), null)) > 0) {
            sqLiteDatabase.close();
            return true;
        } else {
            sqLiteDatabase.close();
            return false;
        }
    }

    public boolean deleteMoney(ThuChi thuChi) {
        sqLiteDatabase = this.getWritableDatabase();
        if (sqLiteDatabase.delete(TBNAME, KEY_ID + " = ? ", new String[]{String.valueOf(thuChi.getId())}) > 0) {
            sqLiteDatabase.close();
            return true;
        }

        sqLiteDatabase.close();
        return false;
    }

    public List<ThuChi> getAllKhoanThu() {
        List<ThuChi> thuChiList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        try {
            String query = "SELECT * FROM " + TBNAME + " WHERE " + TYPEMONEY + " = 1";
            Cursor cursor = database.rawQuery(query, null);
            cursor.moveToFirst();
            do {
                ThuChi thuChi = new ThuChi();
                thuChi.setId(Integer.parseInt(cursor.getString(0)));
                thuChi.setNote(cursor.getString(1));
                thuChi.setDanhmuc(cursor.getString(2));
                thuChi.setNumber(cursor.getString(3));
                thuChi.setDate(cursor.getString(4));
                thuChi.setType(Integer.parseInt(cursor.getString(5)));
                thuChi.setTypeSpiner(Integer.parseInt(cursor.getString(6)));
                thuChiList.add(thuChi);
            }
            while (cursor.moveToNext());
            database.close();
        } catch (Exception e) {
        }
        return thuChiList;

    }

    public List<ThuChi> getAllKhoanChi() {

        List<ThuChi> thuChiList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        try {
            String query = "SELECT * FROM " + TBNAME + " WHERE " + TYPEMONEY + " = 2";
            Cursor cursor = database.rawQuery(query, null);
            cursor.moveToFirst();
            do {
                ThuChi thuChi = new ThuChi();
                thuChi.setId(Integer.parseInt(cursor.getString(0)));
                thuChi.setNote(cursor.getString(1));
                thuChi.setDanhmuc(cursor.getString(2));
                thuChi.setNumber(cursor.getString(3));
                thuChi.setDate(cursor.getString(4));
                thuChi.setType(Integer.parseInt(cursor.getString(5)));
                thuChi.setTypeSpiner(Integer.parseInt(cursor.getString(6)));
                thuChiList.add(thuChi);
            }
            while (cursor.moveToNext());
            database.close();
        } catch (Exception e) {
        }
        return thuChiList;
    }

    public Float getTongThu() {
        SQLiteDatabase database = this.getReadableDatabase();
        float tong = 0;
        try {
            String query = "SELECT sum(Number) FROM " + TBNAME + " WHERE " + TYPEMONEY + " = 1";
            Cursor cursor = database.rawQuery(query, null);
            cursor.moveToFirst();
            String i = cursor.getString(0);
            float n = Float.parseFloat(i);
            tong = n;
            database.close();
        } catch (Exception e) {
        }
        return tong;
    }

    public Float getTongChi() {
        SQLiteDatabase database = this.getReadableDatabase();
        float tong = 0;
        try {
            String query = "SELECT sum(Number) FROM " + TBNAME + " WHERE " + TYPEMONEY + " = 2";
            Cursor cursor = database.rawQuery(query, null);
            cursor.moveToFirst();
            String i = cursor.getString(0);
            float n = Float.parseFloat(i);
            tong = n;
            database.close();
        } catch (Exception e) {
        }
        return tong;
    }

    public Float getTongThuTheoThangNam(String m) {
        SQLiteDatabase database = this.getReadableDatabase();
        float tong = 0;
        try {
            String query = "SELECT sum("+KEY_NUMBER+") from " +TBNAME+" where "+TYPEMONEY+"=1 and "+KEY_DATE+" like " + m;
            Log.e("qurry", query);
            Cursor cursor = database.rawQuery(query, null);
            cursor.moveToFirst();
            String i = cursor.getString(0);
            float n = Float.parseFloat(i);
            tong = n;
            Log.e("qurry", i);
            database.close();
        } catch (Exception e) {
        }
        return tong;
    }

    public Float getTongChiTheoThangNam(String m) {
        SQLiteDatabase database = this.getReadableDatabase();
        float tong = 0;
        try {
            String query = "SELECT sum("+KEY_NUMBER+") from " +TBNAME+" where "+TYPEMONEY+"=2 and "+KEY_DATE+" like " + m;
            Cursor cursor = database.rawQuery(query, null);
            cursor.moveToFirst();
            String i = cursor.getString(0);
            float n = Float.parseFloat(i);
            tong = n;
            database.close();
        } catch (Exception e) {
        }
        return tong;
    }

    public void getTongThuTheoThang(String m) {
        Commom.lstNumberT.clear();
        Commom.lstDateT.clear();
        ArrayList<String> lstNumber = new ArrayList<>();
        ArrayList<String> lstDate = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        try {
            String query = "SELECT "+KEY_NUMBER+","+KEY_DATE+" from " +TBNAME+" where "+TYPEMONEY+"=1 and "+KEY_DATE+" like " + m;

            Cursor cursor = database.rawQuery(query, null);
            cursor.moveToFirst();
            do {
                String number = cursor.getString(0);
                String date = cursor.getString(1);
                int index = date.lastIndexOf("-");
                String d = date.substring(index + 1);
                Log.e("ngayThu", d);
                lstNumber.add(number);
                lstDate.add(d);
            }
            while (cursor.moveToNext());
            Commom.lstNumberT = lstNumber;
            Commom.lstDateT = lstDate;
            database.close();
        } catch (Exception e) {
            Log.e("n:", e.getMessage());
        }
    }

    public void getTongChiTheoThang(String m) {
        Commom.lstNumberC.clear();
        Commom.lstDateC.clear();
        ArrayList<String> lstNumber = new ArrayList<>();
        ArrayList<String> lstDate = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        try {
            String query = "SELECT "+KEY_NUMBER+","+KEY_DATE+" from " +TBNAME+" where "+TYPEMONEY+"=2 and "+KEY_DATE+" like " + m;

            Cursor cursor = database.rawQuery(query, null);
            cursor.moveToFirst();
            do {
                String number = cursor.getString(0);
                String date = cursor.getString(1);
                int index = date.lastIndexOf("-");
                String d = date.substring(index + 1);
                Log.e("ngay", d);
                lstNumber.add(number);
                lstDate.add(d);
            }
            while (cursor.moveToNext());
            Commom.lstNumberC = lstNumber;
            Commom.lstDateC = lstDate;
            database.close();
        } catch (Exception e) {
            Log.e("n:", e.getMessage());
        }
    }

    public ArrayList<Float> getTongThuTheoNgay(String begin, String end) {
        ArrayList<Float> lstNumber = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        try {
            String query = "SELECT "+KEY_NUMBER+","+KEY_DATE+" from " +TBNAME+" where "+TYPEMONEY+"=1 and "+KEY_DATE+" BETWEEN " + begin + " AND " + end;

            Cursor cursor = database.rawQuery(query, null);
            cursor.moveToFirst();
            do {
               float number = Float.parseFloat(cursor.getString(0));
               String date=cursor.getString(1);
               Commom.lstDateT.add(date);
                lstNumber.add(number);
            }
            while (cursor.moveToNext());
            database.close();
        } catch (Exception e) {
            Log.e("n:", e.getMessage());
        }
        return lstNumber;
    }

    public ArrayList<Float> getTongChiTheoNgay(String begin, String end) {
        ArrayList<Float> lstNumber = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        try {
            String query = "SELECT "+KEY_NUMBER+","+KEY_DATE+" from " +TBNAME+" where "+TYPEMONEY+"=2 and "+KEY_DATE+" BETWEEN " + begin + " AND " + end;

            Cursor cursor = database.rawQuery(query, null);
            cursor.moveToFirst();
            do {
                float number = Float.parseFloat(cursor.getString(0));
                String date=cursor.getString(1);
                Commom.lstDateC.add(date);
                lstNumber.add(number);
            }
            while (cursor.moveToNext());
            database.close();
        } catch (Exception e) {
            Log.e("n:", e.getMessage());
        }
        return lstNumber;
    }
}
