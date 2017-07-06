package pranav.apps.amazing.meterreader.dbmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

import pranav.apps.amazing.meterreader.pojo.Details;
import pranav.apps.amazing.meterreader.pojo.Reading;

/**
 * this local database corresponds to the entries stored after user visits the field
 */

public class DBManagerLocal extends SQLiteOpenHelper {

    private Context context;


    public DBManagerLocal(Context context, SQLiteDatabase.CursorFactory factory, int version) {

        super(context, "iit_mandi.db", factory, 2);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS data (id INTEGER PRIMARY KEY AUTOINCREMENT, flat_id TEXT,taken_by TEXT,taken_on TEXT," +
                "new_reading TEXT,remarks TEXT,status TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS data");
        onCreate(db);
    }

    public boolean checkIfPresent(Reading reading) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM data WHERE flat_id = \"" + reading.getFlat_id() + "\" AND taken_by = \"" + reading.getTakenBy() + "\"" +
                "AND new_reading = \"" + reading.getNewReading() + "\";", null);
        if (cursor.getCount() == 0) {
            cursor.close();
            return false;
        }
        return true;
    }

    public boolean add(Reading reading) {

        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM data ;", null);

        ContentValues contentValues = new ContentValues();

        contentValues.put("flat_id", reading.getFlat_id());
        contentValues.put("taken_by", reading.getTakenBy());
        contentValues.put("taken_on", reading.getTakenOn());
        contentValues.put("new_reading", reading.getNewReading());
        contentValues.put("remarks", reading.getRemarks());
        contentValues.put("status", reading.getStatus());
        db.insert("data", null, contentValues);

        cursor.close();
        return true;
    }

    public ArrayList<Reading> get() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM data;", null);
        cursor.moveToFirst();

        ArrayList<Reading> information = new ArrayList<>();
        //String[] strings = new String[cursor.getCount()];
        if (cursor.getCount() != 0) {
            while (!cursor.isAfterLast()) {
                information.add(new Reading(cursor.getString(cursor.getColumnIndex("flat_id")), cursor.getString(cursor.getColumnIndex("new_reading")),
                        cursor.getString(cursor.getColumnIndex("taken_on")), cursor.getString(cursor.getColumnIndex("remarks")),
                        cursor.getString(cursor.getColumnIndex("taken_by")), cursor.getString(cursor.getColumnIndex("status"))));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return information;
    }



    public boolean setStatus(Reading reading, String s){
        //DatabaseUtils.sqlEscapeString(list);
        SQLiteDatabase db = getWritableDatabase();
        Cursor c =  db.rawQuery( "SELECT * FROM data WHERE taken_on = \""+reading.getTakenOn()+"\";", null);
        c.moveToFirst();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", s);
        db.update("data",contentValues,"taken_on = \"" + reading.getTakenOn() + "\"",null);
        c.close();
        return true;
    }

}