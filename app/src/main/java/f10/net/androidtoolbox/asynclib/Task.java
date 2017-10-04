package f10.net.androidtoolbox.asynclib;

/**
 * Created by fl0 on 05/04/2017.
 */

public interface Task<T> {

    void perform(Promise<T> promise);

}
