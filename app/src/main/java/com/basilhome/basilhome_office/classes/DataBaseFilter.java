package com.basilhome.basilhome_office.classes;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DataBaseFilter extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static String TAG = "DataBaseHelper";
    private static String DB_PATH = "";
    private static String DB_NAME = "City";
    private  Context mContext;
    private SQLiteDatabase mDataBase;

    public DataBaseFilter(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

        this.mContext = context;
        if (android.os.Build.VERSION.SDK_INT >= 17) {
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        } else {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
        this.mContext = context;
        try {
            createDataBase();
        } catch (Exception e) {
            Log.d(TAG, "DataBaseHelper: can not create database");
        }
    }
    public ArrayList<String> getOstan() {
        ArrayList<String> arrayList = new ArrayList<String>();
        if (open()) {
            Cursor cursor = mDataBase.rawQuery("select name from province", null);
            if (!cursor.moveToFirst()) {
                cursor.close();
                close();
            } else {

                do {
                    arrayList.add(cursor.getString(0));
                } while (cursor.moveToNext());
                cursor.close();
                close();
            }
        }
        return arrayList;
    }

    public ArrayList<String> getcity(int id) {
        ArrayList<String> arrayList = new ArrayList<String>();
        if (open()) {
            Cursor cursor = mDataBase.rawQuery("select name from city where province_id='"+id+"'", null);
            if (!cursor.moveToFirst()) {
                cursor.close();
                close();
            } else {
                do {
                    arrayList.add(cursor.getString(0));
                } while (cursor.moveToNext());
                cursor.close();
                close();
            }
        }
        return arrayList;
    }

    public int getostanid(String name) {
        int id=0;
        if (open()) {
            Cursor cursor = mDataBase.rawQuery("select id from province where name='"+name+"'", null);
            if (!cursor.moveToFirst()) {
                cursor.close();
                close();
            } else {
                do {
                    id=(cursor.getInt(0));
                } while (cursor.moveToNext());
                cursor.close();
                close();
            }
        }
        return id;
    }

    @Override
    public synchronized void close() {
        if (mDataBase != null)
            mDataBase.close();
        super.close();
    }

    public boolean open() throws SQLException {
        try {
            String mPath = DB_PATH + DB_NAME;
            mDataBase = SQLiteDatabase.openDatabase(mPath, null,
                    SQLiteDatabase.CREATE_IF_NECESSARY);
            return mDataBase != null;
        } catch (Exception e) {
        }
        return false;
    }

    private void createDataBase() {
        try {
            boolean mDataBaseExist = checkDataBase();
            if (!mDataBaseExist) {
                this.getReadableDatabase();
                this.close();
                copyDataBase();
            }
        } catch (Exception e) {
        }
    }

    private void copyDataBase() {
        try {
            InputStream mInput = mContext.getAssets().open("City.db");
            String outFileName = DB_PATH + DB_NAME;
            OutputStream mOutput = new FileOutputStream(outFileName);
            byte[] mBuffer = new byte[1024];
            int mLength;
            while ((mLength = mInput.read(mBuffer)) > 0) {
                mOutput.write(mBuffer, 0, mLength);
            }
            mOutput.flush();
            mOutput.close();
            mInput.close();

        } catch (Exception e) {

        }
    }
    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE="CREATE TABLE IF NOT EXISTS  city(" +
                "[id] [int] NOT NULL," +
                "[province_id] [int] NOT NULL," +
                "[name] varchar(200) NOT NULL)" ;
            db.execSQL(CREATE_TABLE);
            CREATE_TABLE="INSERT INTO city ('id', 'province_id', 'name') VALUES" +
                "(0, 0, 'انتخاب نمایید')," +
                "(1, 1, 'خرید')," +
                "(2, 1, 'فروش')," +
                "(3, 1, 'اجاره')," +
                "(4, 2, 'شهروندی')," +
                "(5, 2, 'توریستی')," +
                "(6, 2, 'کاری')," +
                "(7, 3, 'ثبت شرکت')," +
                "(8, 3, 'امور گمرکی')," +
                "(9, 3, 'امور بانکی')," +
                "(10, 4, 'اخذ گواهینامه')," +
                "(11, 4, 'ترجمه رسمی')," +
                "(12, 4, 'امور ثبت محضری');";

            db.execSQL(CREATE_TABLE);
            CREATE_TABLE = "CREATE TABLE IF NOT EXISTS  province("
                    + "[ID] [int] NOT NULL,"+ "[name] [ntext] )"
            ;
            db.execSQL(CREATE_TABLE);
            CREATE_TABLE="INSERT INTO 'province' ('id', 'name') VALUES" +
                    "(0, 'انتخاب نمایید')," +
                    "(1, 'امور ملکی')," +
                    "(3, 'اقامت')," +
                    "(4, 'تجاری')," +
                    "(5, 'خدمات دیگر');";
            db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_NAME);
        onCreate(db);
    }
}
