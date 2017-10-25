package f10.net.androidtoolbox.Logger;

import android.support.annotation.Nullable;

import com.android.internal.util.Predicate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by fl0 on 22/03/2017.
 */

public class Log {


    private static Log instance = null;

    public static Log getInstance(){
        if(instance == null)
        {
            instance = new Log();
        }
        return instance;
    }

    public static void d(String text)
    {
        Log.getInstance().publish(new LoggerEvent("logger", text));
    }

    public static void d(String tag, String text)
    {
        Log.getInstance().publish(new LoggerEvent(tag, text));
    }

    private final ArrayList<LoggerEvent> _events = new ArrayList<LoggerEvent>();

    public void publish(LoggerEvent event)
    {
        android.util.Log.d("LOGGER: " + event.group, event.description);
        _events.add(event);
    }

    public void clear()
    {
        _events.clear();
    }

    public List<LoggerEvent> find(@Nullable Predicate<LoggerEvent> predicate)
    {
        ArrayList<LoggerEvent> events = new ArrayList<LoggerEvent>();

        for (LoggerEvent e : _events) {
            boolean passes = (predicate == null || predicate.apply(e));
            if(passes) events.add(e);
        }

        Collections.sort(events);

        return events;
    }

}
