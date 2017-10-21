package f10.net.androidtoolbox.eventDriven;

/**
 * Created by fl0 on 20/10/2017.
 */

public class ObservableValue<T> extends ObservableObject {

    private T value;

    public void setValue(T value)
    {
        this.value = value;
        notifyDataChanged();
    }

    public T getValue()
    {
        return value;
    }
}
