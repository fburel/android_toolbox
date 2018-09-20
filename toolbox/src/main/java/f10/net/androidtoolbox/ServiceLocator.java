package f10.net.androidtoolbox;

import java.util.HashMap;

/**
 * Created by fl0 on 10/05/2017.
 */

public class ServiceLocator {

    public interface IService
    {

    }

    // singleton ...

    private static class ServiceLocatorHolder {
        public static final ServiceLocator INSTANCE = new ServiceLocator();
    }

    private ServiceLocator(){}

    // Service locator

    private HashMap<String, Object> services =  new HashMap<>();

    public static <T extends IService> T get(Class<? extends T> contract)
    {
        String name = contract.getName();

        if(ServiceLocatorHolder.INSTANCE.services.containsKey(name)) {
            return (T) ServiceLocatorHolder.INSTANCE.services.get(name);
        }
        assert true;
        return null;
    }

    public static <T extends IService> void put(Class<? extends T> contract,  T service)
    {
        ServiceLocatorHolder.INSTANCE.services.put(contract.getName(), service);
    }

}
