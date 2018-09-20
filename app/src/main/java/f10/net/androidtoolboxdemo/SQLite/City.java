package f10.net.androidtoolboxdemo.SQLite;

import java.io.Serializable;

import f10.net.androidtoolbox.sqlite.SQLiteEntity;

public class City extends SQLiteEntity implements Serializable {

    private String name;
    private double longitude;
    private double latitude;

    public City(String name)
    {
        this.name = name;
    }

    public City(String name, double longitude, double latitude) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }
}
