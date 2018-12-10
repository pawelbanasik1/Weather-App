package mobilki.weather_app.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;


public class DaoLong implements Dao<LongTerm> {

//public class DaoCurrent implements Dao<LongTerm>{
    private static final String INSERT =
            "insert into " + LongTermTab.TABLE_NAME
                    + "(" + LongTermTab.CurrentColumns.TEMP1 + ", " + LongTermTab.CurrentColumns.TEMP2 + ", "
                    + LongTermTab.CurrentColumns.TEMP3 + ", " + LongTermTab.CurrentColumns.TEMP4 + ", "
                    + LongTermTab.CurrentColumns.DATE1 + ", " +  LongTermTab.CurrentColumns.DATE2 + ", "
                    + LongTermTab.CurrentColumns.DATE3 + ", " +  LongTermTab.CurrentColumns.DATE4
                    + ") values (?,?,?,?,?,?,?,?)";
    private SQLiteDatabase db;
    private SQLiteStatement insertStatement;

    public DaoLong(SQLiteDatabase db) {
        this.db = db;
        insertStatement = db.compileStatement(DaoLong.INSERT);
    }

    @Override
    public void save(LongTerm weather) {


        ContentValues values = new ContentValues();

        values.put(LongTermTab.CurrentColumns.TEMP1, weather.getTemp1());
        values.put(LongTermTab.CurrentColumns.TEMP2, weather.getTemp2());
        values.put(LongTermTab.CurrentColumns.TEMP3, weather.getTemp3());
        values.put(LongTermTab.CurrentColumns.TEMP4, weather.getTemp4());
        values.put(LongTermTab.CurrentColumns.DATE1, weather.getDate1());
        values.put(LongTermTab.CurrentColumns.DATE2, weather.getDate2());
        values.put(LongTermTab.CurrentColumns.DATE3, weather.getDate3());
        values.put(LongTermTab.CurrentColumns.DATE4, weather.getDate4());

        Long Id = (Long) db.insert(LongTermTab.TABLE_NAME, null, values);
        weather.setId(Id);

/*        this.InsertGroups(student);
        insertStatement.clearBindings();
        insertStatement.bindString(0, entity.getName());
        insertStatement.bindString(1, entity.getLastName());
        return insertStatement.executeInsert();*/
    }



    @Override
    public LongTerm get(long id) {
        LongTerm weather = null;
        Cursor c =
                db.query(LongTermTab.TABLE_NAME,
                        new String[]{
                                BaseColumns._ID,
                                LongTermTab.CurrentColumns.TEMP1,
                                LongTermTab.CurrentColumns.TEMP2,
                                LongTermTab.CurrentColumns.TEMP3,
                                LongTermTab.CurrentColumns.TEMP4,
                                LongTermTab.CurrentColumns.DATE1,
                                LongTermTab.CurrentColumns.DATE2,
                                LongTermTab.CurrentColumns.DATE3,
                                LongTermTab.CurrentColumns.DATE4,
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


    private LongTerm buildStudentFromCursor(Cursor c) {
        LongTerm Students = null;
        if (c != null) {
            Students = new LongTerm();
            Students.setId(c.getLong(0));
            Students.setTemp1(c.getString(1));
            Students.setTemp2(c.getString(2));
            Students.setTemp3(c.getString(3));
            Students.setTemp4(c.getString(4));
            Students.setDate1(c.getString(5));
            Students.setDate2(c.getString(6));
            Students.setDate3(c.getString(7));
            Students.setDate4(c.getString(8));

        }
        return Students;
    }


}
