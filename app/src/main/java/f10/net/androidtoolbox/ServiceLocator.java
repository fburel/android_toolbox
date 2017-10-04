package f10.net.androidtoolbox;

import java.util.HashMap;

/**
 * Created by fl0 on 10/05/2017.
 */

public class ServiceLocator {

    // singleton ...

    private static class ServiceLocatorHolder {
        public static final ServiceLocator INSTANCE = new ServiceLocator();
    }

    private ServiceLocator(){}

    // Service locator

    private HashMap<String, Object> services =  new HashMap<>();

    public static Object get(String reference)
    {

        if(ServiceLocatorHolder.INSTANCE.services.containsKey(reference)) {
            return ServiceLocatorHolder.INSTANCE.services.get(reference);
        }

        assert true;

        return null;

    }

    public static void put(String reference, Object service)
    {
        ServiceLocatorHolder.INSTANCE.services.put(reference, service);
    }

}
