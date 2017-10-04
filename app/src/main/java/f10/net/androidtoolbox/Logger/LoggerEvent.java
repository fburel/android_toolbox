package f10.net.androidtoolbox.Logger;

import java.util.Date;

/**
 * Created by fl0 on 22/03/2017.
 */
public class LoggerEvent implements Comparable<LoggerEvent>{

    public final Date timestamp;
    public final String group;
    public final String description;

    public LoggerEvent(String group, String eventMessage)
    {
        this.group = group;
        this.description = eventMessage;
        this.timestamp = new Date();
    }

    @Override
    public int compareTo(LoggerEvent o) {
        return o.timestamp.compareTo(timestamp);
    }
}
