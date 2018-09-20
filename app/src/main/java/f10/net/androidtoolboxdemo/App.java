package f10.net.androidtoolboxdemo;

import android.app.Application;

import f10.net.androidtoolbox.ServiceLocator;
import f10.net.androidtoolboxdemo.IServices.ICityRepository;
import f10.net.androidtoolboxdemo.SQLite.CitiesRepository;

public class App extends Application {

    @Override
    public void onCreate() {

        super.onCreate();

        CitiesRepository repo = new CitiesRepository(this);
        if(repo.getAllCities().isEmpty())
        {
            repo.AddCity("Tokyo", 139.6917100, 35.6895000);
            repo.AddCity("Budapest", 19.0399100, 47.4980100);
            repo.AddCity("Paris", 0, 0);
            repo.AddCity("Los Angeles", 0, 0);
            repo.AddCity("Lima", 0, 0);

        }

        ServiceLocator.put(ICityRepository.class, repo);


    }
}
