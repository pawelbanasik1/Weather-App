package mobilki.weather_app.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;

public class DaoCurrent implements Dao<CurrentWeather>{
    private static final String INSERT =
            "insert into " + CurrentWeatherTab.TABLE_NAME
                    + "(" + CurrentWeatherTab.CurrentColumns.CITY + ", " + CurrentWeatherTab.CurrentColumns.TEMP + ", "
                    + CurrentWeatherTab.CurrentColumns.HUM + ", " + CurrentWeatherTab.CurrentColumns.PRES + ", "
                    + CurrentWeatherTab.CurrentColumns.CLOUD
                    + ") values (?,?,?,?,?)";
    private SQLiteDatabase db;
    private SQLiteStatement insertStatement;

    public DaoCurrent(SQLiteDatabase db) {
        this.db = db;
        insertStatement = db.compileStatement(DaoCurrent.INSERT);
    }

    @Override
    public void save(CurrentWeather weather) {


        ContentValues values = new ContentValues();

        values.put(CurrentWeatherTab.CurrentColumns.CITY, weather.getCity());
        values.put(CurrentWeatherTab.CurrentColumns.TEMP, weather.getTemp());
        values.put(CurrentWeatherTab.CurrentColumns.HUM, weather.getHum());
        values.put(CurrentWeatherTab.CurrentColumns.PRES, weather.getPres());
        values.put(CurrentWeatherTab.CurrentColumns.CLOUD, weather.getCloud());

        Long Id = (Long) db.insert(CurrentWeatherTab.TABLE_NAME, null, values);
        weather.setId(Id);

/*        this.InsertGroups(student);
        insertStatement.clearBindings();
        insertStatement.bindString(0, entity.getName());
        insertStatement.bindString(1, entity.getLastName());
        return insertStatement.executeInsert();*/
    }



    @Override
    public CurrentWeather get(long id) {
        CurrentWeather weather = null;
        Cursor c =
                db.query(CurrentWeatherTab.TABLE_NAME,
                        new String[]{
                                BaseColumns._ID,
                                CurrentWeatherTab.CurrentColumns.CITY,
                                CurrentWeatherTab.CurrentColumns.TEMP,
                                CurrentWeatherTab.CurrentColumns.HUM,
                                CurrentWeatherTab.CurrentColumns.PRES,
                                CurrentWeatherTab.CurrentColumns.CLOUD,
                                },
                        BaseColumns._ID + " = ?", new String[]{String.valueOf(id)},
                        null, null, null, "1");
        if (c.moveToFirst()) {
            weather = this.buildStudentFromCursor(c);
        }
        if (!c.isClosed()) {
            c.close();
        }
        return weather;
    }


    private CurrentWeather buildStudentFromCursor(Cursor c) {
        CurrentWeather Students = null;
        if (c != null) {
            Students = new CurrentWeather();
            Students.setId(c.getLong(0));
            Students.setCity(c.getString(1));
            Students.setTemp(c.getString(2));
            Students.setHum(c.getString(3));
            Students.setPres(c.getString(4));
            Students.setCloud(c.getString(5));
        }
        return Students;
    }


}
