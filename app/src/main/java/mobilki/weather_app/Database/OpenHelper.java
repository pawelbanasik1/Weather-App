package mobilki.weather_app.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;


public class OpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "sqliteApp.db";
    private static final int DATABASE_VERSION = 1;
    private Context context;
    SQLiteDatabase db;


    public OpenHelper(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }
    @Override
    public void onOpen(final SQLiteDatabase db) {

        super.onOpen(db);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        this.db = db;

        Log.v("ujamuja1", "ujamuj2");
        CurrentWeatherTab.onCreate(db);
//      GroupTable.onCreate(db);
    }
    @Override
    public void onUpgrade(final SQLiteDatabase db,
                          final int oldVersion, final int newVersion) {

        CurrentWeatherTab.onUpgrade(db, oldVersion, newVersion);

    }



}


