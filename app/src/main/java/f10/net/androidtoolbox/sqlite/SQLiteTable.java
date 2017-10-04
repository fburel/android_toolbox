package f10.net.androidtoolbox.sqlite;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fl0 on 09/05/2017.
 */

public class SQLiteTable<T extends SQLiteEntity> {

    public static final String TypeINTEGER = "INTEGER"; // The value is a signed integer, stored in 1, 2, 3, 4, 6, or 8 bytes depending on the magnitude of the value.
    public static final String TypeREAL = "REAL"; // The value is a floating point value, stored as an 8-byte IEEE floating point number.
    public static final String TypeTEXT = "TEXT"; // The value is a text string, stored using the database encoding (UTF-8, UTF-16BE or UTF-16LE).
    public static final String TypeBLOB = "BLOB"; // The value is a blob of data, stored exactly as it was input.


    static final String PRIMARY_KEY_FIELD = "zKey"; // primary key used to self managed SQLiteEntities

    final String name;                                  // Name of the table in the db
    final ArrayList<String> columns;                    // list of all columns in the table
    private final HashMap<String, String> _extraColums; // list of customer added fieds (name + type)
    private final SQLiteTableSerializer<T> _serializer; // serializer used to map ContentValus <--> Object


    public SQLiteTable(String name, SQLiteTableSerializer<T> serializer) {
        this.name = name;
        columns = new ArrayList<String>();
        columns.add(PRIMARY_KEY_FIELD);

        _extraColums = new HashMap<String, String>();
        _serializer = serializer;
        _serializer.onCreateTable(this);
    }

    public void addColumn(String name, String Type) {
        _extraColums.put(name, Type);
        columns.add(name);
    }

    String createStatement()
    {
        StringBuilder str = new StringBuilder();
        str.append("CREATE TABLE ").append(name).append(" (").append(PRIMARY_KEY_FIELD).append(" INTEGER PRIMARY KEY AUTOINCREMENT");

        for(Map.Entry<String, String> field : _extraColums.entrySet())
        {
            str.append(", ").append(field.getKey()).append(" ").append(field.getValue());
        }
        str.append(");");

        return str.toString();
    }

    ArrayList<T> parseCursor(Cursor c) {

        ArrayList<T> result = new ArrayList<T>();

        c.moveToFirst();
        while(!c.isAfterLast())
        {
            ContentValues o  = new ContentValues();

            for(String columnName : columns)
            {
                if(columnName.equals(PRIMARY_KEY_FIELD)){continue;}

                String columnType = _extraColums.get(columnName);

                int columnPosition = columns.indexOf(columnName);

                if(columnType.equals(TypeINTEGER))
                {
                    o.put(columnName, c.getInt(columnPosition));
                }
                else if(columnType.equals(TypeREAL))
                {
                    o.put(columnName, c.getFloat(columnPosition));
                }
                else if(columnType.equals(TypeTEXT))
                {
                    o.put(columnName, c.getString(columnPosition));
                }
                else if(columnType.equals(TypeBLOB))
                {
                    o.put(columnName, c.getBlob(columnPosition));
                }
            }

            T object = _serializer.deserialize(o);
            object.set_zkey(c.getInt(0));

            result.add(object);
            c.moveToNext();
        }

        c.close();

        return result;
    }


    public ContentValues serialize(T object) {

        ContentValues cv = _serializer.serialize(object);

        return cv;
    }
}