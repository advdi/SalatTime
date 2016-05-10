package iak.advdi.salattime;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by bakakkoii on 5/9/16.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static String path = "/data/data/iak.advdi.salattime/databases/";
    public static final String database = "salattime.db";
    public static final String tabel = "salattime";
    public final String kolom_id = "_id";
    public final String kolom_date_for = "date_for";
    public final String kolom_fajr = "fajr";
    public final String kolom_dhuhr = "dhuhr";
    public final String kolom_asr = "asr";
    public final String kolom_maghrib = "maghrib";
    public final String kolom_isha = "isha";
    private final Context context;
    private SQLiteDatabase myDB;

    public DBHelper(Context context) {
        super(context, database, null, 1);
        this.context = context;
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void createDatabase() throws IOException {
        boolean isDatabaseExist = checkDatabase();
        if (isDatabaseExist) {
            //do nothing, database is already exist
        } else {
            this.getReadableDatabase();
            try {
                copyDatabase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    private void copyDatabase() throws IOException {
        InputStream input = context.getAssets().open(database);
        String outputFilename = path + database;
        OutputStream output = new FileOutputStream(outputFilename);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = input.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }
        output.flush();
        output.close();
        input.close();
    }

    private boolean checkDatabase() {
        SQLiteDatabase db = null;
        try {
            String mypath = path + database;
            db = SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {

        }
        //return db != null ? true : false;
        if (db != null)
            db.close();

        return db != null ? true : false;
    }

    public void openDatabase() {
        String mypath = path + database;
        myDB = SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READONLY);
    }

    @Override
    public synchronized void close() {
        if (myDB != null) {
            myDB.close();
        }
        super.close();
    }

    public boolean insert(String date_for, String fajr, String dhuhr, String asr, String maghrib, String isha, String prioritas, String useReminder) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("date_for", date_for);
        cv.put("fajr", fajr);
        cv.put("dhuhr", dhuhr);
        cv.put("asr", asr);
        cv.put("maghrib", maghrib);
        cv.put("isha", isha);
        db.insert("salattime", null, cv);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("select * from salattime where _id=" + id + "", null);
        return cur;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int rowNums = (int) DatabaseUtils.queryNumEntries(db, tabel);
        return rowNums;
    }

    public boolean update(Integer id, String date_for, String fajr, String dhuhr, String asr, String maghrib, String isha) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("date_for", date_for);
        cv.put("fajr", fajr);
        cv.put("dhuhr", dhuhr);
        cv.put("asr", asr);
        cv.put("maghrib", maghrib);
        cv.put("isha", isha);
        db.update("salattime", cv, "_id = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    public Integer delete(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("salattime", "_id = ? ", new String[]{Integer.toString(id)});
    }

    public ArrayList<String> getAllData() {
        myDB.close();
        ArrayList<String> alist = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("select * from salattime", null);
        cur.moveToFirst();

        while (cur.isAfterLast() == false) {
            String s = new String(
                    cur.getString(cur.getColumnIndex(kolom_date_for))
                            /*+ "," +
                    cur.getInt(cur.getColumnIndex(kolom_id)) + "," +
                    cur.getString(cur.getColumnIndex(kolom_fajr)) + "," +
                    cur.getString(cur.getColumnIndex(kolom_dhuhr)) + "," +
                    cur.getString(cur.getColumnIndex(kolom_asr)) + "," +
                    cur.getString(cur.getColumnIndex(kolom_maghrib)) + "," +
                    cur.getString(cur.getColumnIndex(kolom_isha))*/
            );
            alist.add(s);
            cur.moveToNext();
        }
        return alist;
    }

}
