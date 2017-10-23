package f10.net.androidtoolbox.collections;

import java.util.ArrayList;

import f10.net.androidtoolbox.asynclib.Mapper;
import f10.net.androidtoolbox.asynclib.Task;

/**
 * Created by fl0 on 23/10/2017.
 */

public final class ArrayTools {

    public interface Executor<T>{
        void exectue(T object);
    }

    static <T, U> ArrayList<U> map(ArrayList<T> source, Mapper<T, U> mapper)
    {
        ArrayList<U> dest = new ArrayList<>();

        for(T item : source)
        {
            try {
                dest.add(mapper.map(item));
            } catch (Exception ignored) {
                // do nothing
            }
        }

        return dest;
    }

    static <T> void forEach(ArrayList<T> source, Executor<T> executor)
    {
        for(T item : source)
        {
            executor.exectue(item);
        }
    }
}
