package mobilki.weather_app.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataManagerImp implements DataManager{
    private Context context;
    private SQLiteDatabase db;

    private DaoCurrent DaoCurrent;
    // private MovieCategoryDao movieCategoryDao;
    public DataManagerImp(Context context) {
        this.context = context;

        SQLiteOpenHelper openHelper = new OpenHelper(context);
        this.db = openHelper.getWritableDatabase();
        DaoCurrent = new DaoCurrent(db);

        //movieCategoryDao = new MovieCategoryDao(db);
    }

    public CurrentWeather getCurrent(long StudentId) {
        CurrentWeather CurrentWeather = DaoCurrent.get(StudentId);
      /*  if (Student != null) {
            Student.getGroups().addAll(
                    movieCategoryDao.getGroup(Student.getId()));
        }*/
        return CurrentWeather;
    }


    public void saveCurrent (CurrentWeather student) { DaoCurrent.save(student);  }
    /* public void saveStudent(Students Student) {
         long StudentId = 0L;
         StudentId = DaoStudent.save(Student);

         db.beginTransaction();
         StudentId = DaoStudent.save(Student);*/
/*        try {
//            db.beginTransaction();
            StudentId = DaoStudent.save(Student);
         *//*   if (Student.getGroups().size() > 0) {
                for (Group c : Student.getGroups()) {
                    long catId = 0L;
                    Group dbCat = DaoGroup.find(c.getName());
                    if (dbCat == null) {
                        catId = DaoGroup.save(c);
                    } else {
                        catId = dbCat.getId();
                                     }
                    MovieCategoryKey mcKey =
                            new MovieCategoryKey(movieId, catId);
                    if (!movieCategoryDao.exists(mcKey)) {
                        movieCategoryDao.save(mcKey);
                    }
                }
            }*//*
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.e("err",
                    "Błąd przy zapisie filmu (transakcję anulowano)", e);
            StudentId = 0L;
        } finally {
            db.endTransaction();
        }*/
    // return StudentId;
    //}

}
