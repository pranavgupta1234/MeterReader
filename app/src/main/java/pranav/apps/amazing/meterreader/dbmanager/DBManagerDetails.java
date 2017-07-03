package pranav.apps.amazing.meterreader.dbmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

import pranav.apps.amazing.meterreader.pojo.Details;

/** this local database corresponds to the local cached details of the residents present in the institute
 * */

public class DBManagerDetails extends SQLiteOpenHelper {

    private Context context;


    public DBManagerDetails(Context context, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "iit_mandi.db", factory, 2);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS details (id INTEGER PRIMARY KEY AUTOINCREMENT, flat_id TEXT,name TEXT,meter_no TEXT," +
                "old_reading TEXT,new_reading TEXT,unit TEXT,current_charges TEXT,fixed_charges TEXT," +
                "address TEXT,building_no TEXT,flat_no TEXT,remarks TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS details");
        onCreate(db);
    }

    public boolean checkIfPresent(Details details){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM details WHERE flat_id = \""+details.getFlat_id()+"\";",null);
        if(cursor.getCount()==0){
            cursor.close();
            return false;
        }
        return true;
    }

    public boolean add(Details details){

        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM details ;",null);

        Cursor c =  db.rawQuery("SELECT * FROM details WHERE flat_id = \""+details.getFlat_id()+"\";", null);

        c.moveToFirst();
        int count = c.getCount();

        if (count>0) {
            Toast.makeText(context,"Already Present",Toast.LENGTH_SHORT).show();
            return false;
        }

        ContentValues contentValues = new ContentValues();

        contentValues.put("flat_id",details.getFlat_id());
        contentValues.put("name",details.getName());
        contentValues.put("meter_no",details.getMeter_no());
        contentValues.put("old_reading",details.getOld_reading());
        contentValues.put("new_reading",details.getNew_reading());
        contentValues.put("unit",details.getUnit());
        contentValues.put("current_charges",details.getCurrent_charges());
        contentValues.put("fixed_charges",details.getFixed_charges());
        contentValues.put("address",details.getAddress());
        contentValues.put("building_no",details.getBuilding_no());
        contentValues.put("flat_no",details.getFlat_no());
        contentValues.put("remarks",details.getRemarks());
        db.insert("details", null, contentValues);

        c.close();
        cursor.close();
        return true;
    }

    public ArrayList<Details> show(){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery("SELECT * FROM details;", null);
        cursor.moveToFirst();

        ArrayList<Details> information = new ArrayList<>();
        //String[] strings = new String[cursor.getCount()];
        if(cursor.getCount()!=0) {
            while (!cursor.isAfterLast()) {
                information.add(new Details(cursor.getString(cursor.getColumnIndex("flat_id")),cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("meter_no")),cursor.getString(cursor.getColumnIndex("old_reading")),
                        cursor.getString(cursor.getColumnIndex("new_reading")),cursor.getString(cursor.getColumnIndex("unit")),
                        cursor.getString(cursor.getColumnIndex("current_charges")),cursor.getString(cursor.getColumnIndex("fixed_charges")),
                        cursor.getString(cursor.getColumnIndex("address")),cursor.getString(cursor.getColumnIndex("building_no")),
                        cursor.getString(cursor.getColumnIndex("flat_no")),cursor.getString(cursor.getColumnIndex("remarks"))));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return information;
    }

    public void delete(Details details){

        SQLiteDatabase db = getWritableDatabase();
        Cursor c =  db.rawQuery( "SELECT * FROM details WHERE flat_id = \""+ details.getFlat_id()+"\";", null);
        c.moveToFirst();

        db.execSQL("DELETE * FROM details WHERE flat_id = \""+details.getFlat_id()+"\";");
    }

}