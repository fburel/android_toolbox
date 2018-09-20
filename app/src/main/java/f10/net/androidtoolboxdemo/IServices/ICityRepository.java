package f10.net.androidtoolboxdemo.IServices;

import java.util.ArrayList;

import f10.net.androidtoolbox.ServiceLocator;
import f10.net.androidtoolboxdemo.SQLite.City;

public interface ICityRepository extends ServiceLocator.IService
{
    ArrayList<City> getAllCities();

    void AddCity(String name, double longitude, double latitude);

    void Update(City city);
}
