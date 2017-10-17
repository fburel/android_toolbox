package f10.net.androidtoolbox.asynclib;

/**
 * Created by fl0 on 17/10/2017.
 */

public interface Mapper<T, U> {
    U map(T result) throws Exception;
}
