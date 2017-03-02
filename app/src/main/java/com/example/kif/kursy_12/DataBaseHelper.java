package com.example.kif.kursy_12;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kif on 12.01.2017.
 */

public class DataBaseHelper extends SQLiteOpenHelper {
    private SQLiteDatabase mDataBase;


    public DataBaseHelper(Context context) {
        super(context, "MyDB3.db", null, 1);
        mDataBase = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Student.TABLE_NAME+ " (" +
                 Student.COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                 Student.COLUMN_FIRST_NAME+ " TEXT NOT NULL," +
                 Student.COLUMN_LAST_NAME+" TEXT NOT NULL," +
                 Student.COLUMN_AGE + " INTEGER NOT NULL);");
    }

    public long insertStudent(Student student){

     long id = 0;
        try{
            ContentValues values = new ContentValues();

            values.put(Student.COLUMN_FIRST_NAME, student.FirstName);
            values.put(Student.COLUMN_LAST_NAME, student.LastName);
            values.put(Student.COLUMN_AGE, student.Age);

            id = mDataBase.insert(Student.TABLE_NAME, null, values);

        }catch (Exception e){
                e.printStackTrace();
        }
        return id;
    }

    public Student getStudent(long id){
        Student student = null;
        Cursor cursor = null;

        try {
            cursor = mDataBase.query(Student.TABLE_NAME, null, Student.COLUMN_ID + "=" + id, null, null,null,null);

            if(cursor.moveToFirst()){
                student = new Student();

                student.id = cursor.getLong(cursor.getColumnIndex(Student.COLUMN_ID));
                student.FirstName = cursor.getString(cursor.getColumnIndex(Student.COLUMN_FIRST_NAME));
                student.LastName = cursor.getString(cursor.getColumnIndex(Student.COLUMN_LAST_NAME));
                student.Age = cursor.getLong(cursor.getColumnIndex(Student.COLUMN_AGE));
            }


        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if(cursor != null){
                cursor.close();
            }
        }


        return student;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
