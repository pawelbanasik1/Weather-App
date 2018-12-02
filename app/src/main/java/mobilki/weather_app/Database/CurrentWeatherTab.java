package mobilki.weather_app.Database;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class CurrentWeatherTab {


    public static final String TABLE_NAME = "current";

    public static class CurrentColumns implements BaseColumns {
        public static final String CITY = "city";
        public static final String TEMP = "temp";
        public static final String HUM = "hum";
        public static final String PRES = "pres";
        public static final String CLOUD = "cloud";
    }

    public static void onCreate(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE " + CurrentWeatherTab.TABLE_NAME + " ( ");
        sb.append(BaseColumns._ID + " INTEGER PRIMARY KEY, ");
        // Nazwy filmów nie są niepowtarzalne, jednak dla uproszczenia
        // stosujemy ograniczenie niepowtarzalności.
        sb.append(CurrentColumns.CITY + " TEXT, ");
        sb.append(CurrentColumns.TEMP + " TEXT, ");
        sb.append(CurrentColumns.HUM + " TEXT, ");
        sb.append(CurrentColumns.PRES + " TEXT, ");
        sb.append(CurrentColumns.CLOUD + " TEXT ");
        sb.append(");");
        db.execSQL(sb.toString());
    }

    public static void onUpgrade(SQLiteDatabase db,
                                 int oldVersion,
                                 int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "
                + CurrentWeatherTab.TABLE_NAME);
        CurrentWeatherTab.onCreate(db);
    }

}
