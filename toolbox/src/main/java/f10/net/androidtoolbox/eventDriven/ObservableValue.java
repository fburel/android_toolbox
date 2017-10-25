package f10.net.androidtoolbox.eventDriven;

/**
 * Created by fl0 on 23/10/2017.
 */

public class ObservableValue<T> extends ObservableObject {

    private T value = null;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
        notifyDataChanged();
    }
}
