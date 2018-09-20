package f10.net.androidtoolboxdemo.SQLite;

import android.content.ContentValues;
import android.content.Context;

import java.util.ArrayList;

import f10.net.androidtoolbox.sqlite.SQLiteHelper;
import f10.net.androidtoolbox.sqlite.SQLiteQuery;
import f10.net.androidtoolbox.sqlite.SQLiteTable;
import f10.net.androidtoolbox.sqlite.SQLiteTableSerializer;
import f10.net.androidtoolboxdemo.IServices.ICityRepository;

public class CitiesRepository implements ICityRepository {

    private SQLiteHelper helper;
    private SQLiteTable<City> tableCity;

    public CitiesRepository(Context ctx) {
        this.helper = new SQLiteHelper(ctx, "Cities", null, 1);
        this.tableCity = new SQLiteTable("City", new SQLiteTableSerializer<City>(){

            @Override
            public City deserialize(ContentValues item) {

                City c = new City(item.getAsString("name"));
                c.setLatitude(item.getAsDouble("latitude"));
                c.setLongitude(item.getAsDouble("longitude"));
                return c;
            }

            @Override
            public ContentValues serialize(City object) {
                ContentValues cv = new ContentValues();
                cv.put("name", object.getName());
                cv.put("longitude", object.getLongitude());
                cv.put("latitude", object.getLatitude());
                return cv;
            }

            @Override
            public void onCreateTable(SQLiteTable<City> table) {
                table.addColumn("name", SQLiteTable.TypeTEXT);
                table.addColumn("longitude", SQLiteTable.TypeREAL);
                table.addColumn("latitude", SQLiteTable.TypeREAL);
            }
        });

        this.helper.addTable(this.tableCity);
    }

    /// Retur all the city from the db, order alphabetically where long/lat are different from 0
    public ArrayList<City> getAllCities() {
        return this.helper.executeQuery(SQLiteQuery.selectFrom(this.tableCity)
                .orderBy("name"));
    }

    @Override
    public void AddCity(String name, double longitude, double latitude) {
        this.helper.createEntity(this.tableCity, new City(name, longitude, latitude));
    }

}
