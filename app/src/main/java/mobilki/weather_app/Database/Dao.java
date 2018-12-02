package mobilki.weather_app.Database;

public interface Dao<T> {
    void save(T type);

    T get(long id);
}