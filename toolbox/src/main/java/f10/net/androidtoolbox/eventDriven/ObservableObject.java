package f10.net.androidtoolbox.eventDriven;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by fl0 on 13/06/2017.
 */

public abstract class ObservableObject {

    public interface Observer {
        void onObservableObjectChanged(ObservableObject observable, Object context);
    }

    private final Set<WeakReference<Observer>> obs = new HashSet<WeakReference<Observer>>();
    private final HashMap<WeakReference<Observer>, Object> ctx = new HashMap<WeakReference<Observer>, Object>();

    public Set<WeakReference<Observer>> getObservers(){
        return Collections.unmodifiableSet(obs);
    }

    public void addObserver(Observer subscriber, Object context) {
        WeakReference<Observer> v = new WeakReference<Observer>(subscriber);
        obs.add(v);
        if(context != null) ctx.put(v, context);
    }

    public void removeAllObservers() {
        obs.clear();
        ctx.clear();
    }

    public void notifyDataChanged()
    {
        for(WeakReference<Observer> s : obs)
        {
            try {
                Object c = ctx.containsKey(s) ? ctx.get(s) : null;
                s.get().onObservableObjectChanged(this, c);
            }
            catch (Exception ignored) {

            }
        }
    }

}
