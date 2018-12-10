package mobilki.weather_app.Database;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class LongTermTab {
//public class CurrentWeatherTab {


    public static final String TABLE_NAME = "long";

    public static class CurrentColumns implements BaseColumns {
        public static final String TEMP1 = "temp1";
        public static final String TEMP2= "temp2";
        public static final String TEMP3 = "temp3";
        public static final String TEMP4 = "temp4";
        public static final String DATE1 = "date1";
        public static final String DATE2 = "date2";
        public static final String DATE3 = "date3";
        public static final String DATE4 = "date4";
    }

    public static void onCreate(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE " + LongTermTab.TABLE_NAME + " ( ");
        sb.append(BaseColumns._ID + " INTEGER PRIMARY KEY, ");

        sb.append(CurrentColumns.TEMP1 + " TEXT, ");
        sb.append(CurrentColumns.TEMP2 + " TEXT, ");
        sb.append(CurrentColumns.TEMP3+ " TEXT, ");
        sb.append(CurrentColumns.TEMP4 + " TEXT, ");
        sb.append(CurrentColumns.DATE1 + " TEXT, ");
        sb.append(CurrentColumns.DATE2 + " TEXT, ");
        sb.append(CurrentColumns.DATE3 + " TEXT, ");
        sb.append(CurrentColumns.DATE4 + " TEXT ");
        sb.append(");");
        db.execSQL(sb.toString());
    }

    public static void onUpgrade(SQLiteDatabase db,
                                 int oldVersion,
                                 int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "
                + LongTermTab.TABLE_NAME);
        LongTermTab.onCreate(db);
    }

}
