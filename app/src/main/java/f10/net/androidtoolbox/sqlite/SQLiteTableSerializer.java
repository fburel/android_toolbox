package f10.net.androidtoolbox.sqlite;

import android.content.ContentValues;

/**
 * Created by fl0 on 09/05/2017.
 */

public interface SQLiteTableSerializer<T extends SQLiteEntity> {

    T deserialize(ContentValues item);

    ContentValues serialize(T object);

    void onCreateTable(SQLiteTable<T> table);
}
