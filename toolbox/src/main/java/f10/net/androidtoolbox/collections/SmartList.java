package f10.net.androidtoolbox.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import f10.net.androidtoolbox.interfaces.Predicate;

import f10.net.androidtoolbox.asynclib.Mapper;
import f10.net.androidtoolbox.interfaces.Executor;
import f10.net.androidtoolbox.interfaces.SortDescripor;

/**
 * Created by fl0 on 24/10/2017.
 */

public class SmartList<T> extends ArrayList<T> {

    public SmartList(int initialCapacity) {
        super(initialCapacity);
    }

    public SmartList() {
        super();
    }

    public SmartList(Collection<? extends T> c) {
        super(c);
    }

    public SmartList(T[] collection)
    {
        super(Arrays.asList(collection));
    }

    public SmartList(T item) {
        super();
        add(item);
    }

    public <U> SmartList<U> map(Mapper<T, U> mapper)
    {
        SmartList<U> dest = new SmartList<>();

        for(T item : this)
        {
            try {
                dest.add(mapper.map(item));
            } catch (Exception ignored) {
                // do nothing
            }
        }

        return dest;
    }

    public void forEach(Executor<T> executor)
    {
        for(T item : this)
        {
            executor.executeWith(item);
        }
    }

    public void filter(Predicate<T> filter)
    {
        SmartList<T> toRemove = new SmartList<>();
        for(T item : this)
        {
            if(!filter.accept(item)){
                toRemove.add(item);
            }
        }
        this.removeAll(toRemove);
    }

    public T first()
    {
        return firstOrDefault(null);
    }

    public T last()
    {
        if(this.size() != 0)
        {
            return this.get(this.size() - 1);
        }
        return null;
    }

    public T any()
    {
        if(this.size() == 0) return null;
        int index = new Random().nextInt(this.size());
        return this.get(index);
    }

    public T firstOrDefault(T _default)
    {
        if(this.size() != 0)
        {
            return this.get(0);
        }
        return _default;
    }

    public void sort(final SortDescripor<T> descriptor)
    {
        if(this.size() <= 1) return;

        final SmartList<T> before = new SmartList<>();
        final SmartList<T> after = new SmartList<>();

        final T pivot = this.any();

        this.forEach(new Executor<T>() {
            @Override
            public void executeWith(T object) {
                if(descriptor.isBefore(object, pivot))
                {
                    before.add(object);
                }
                else
                {
                    after.add(object);
                }
            }
        });

        before.sort(descriptor);
        after.sort(descriptor);

        this.clear();
        this.addAll(before);
        this.addAll(after);
    }

    public void swap(int position1, int position2)
    {
        T obj1 = this.get(position1);
        T obj2 = this.get(position2);
        this.add(position1, obj2);
        this.add(position2, obj1);
    }

    public <K> Map<K, SmartList<T>> group(final Mapper<T, K> groupby)
    {
        final Map<K, SmartList<T>> map = new HashMap<>();
        this.forEach(new Executor<T>() {
            @Override
            public void executeWith(T object) {
                try {
                    K keyforObject = groupby.map(object);
                    if(!map.containsKey(keyforObject))
                    {
                        map.put(keyforObject, new SmartList<T>());
                    }
                    SmartList<T> list = map.get(keyforObject);
                    list.add(object);
                } catch (Exception ignored) {
                    // do nothing
                }
            }
        });
        return map;

    }
}
