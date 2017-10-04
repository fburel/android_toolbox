package f10.net.androidtoolbox;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by fl0 on 13/06/2017.
 */

public abstract class ObservableObject {

    public interface Observer {
        void onObservableObjectChanged(ObservableObject observable);
    }

    private final Set<WeakReference<Observer>> obs = new HashSet<WeakReference<Observer>>();

    public Set<WeakReference<Observer>> getObservers(){
        return Collections.unmodifiableSet(obs);
    }

    public void addObserver(Observer subscriber) {
        obs.add(new WeakReference<Observer>(subscriber));
    }

    public void removeAllObservers() {
        obs.clear();
    }

    public void notifyDataChanged()
    {
        for(WeakReference<Observer> s : obs)
        {
            try {
                s.get().onObservableObjectChanged(this);
            }
            catch (Exception ignored) {

            }
        }
    }

}
