package f10.net.androidtoolbox.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by fl0 on 09/05/2017.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    ArrayList<SQLiteTable> _tables;

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {

        super(context, name, factory, version);

        _tables = new ArrayList<SQLiteTable>();
    }

    public void addTable(SQLiteTable table)
    {
        _tables.add(table);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //on crée la table à partir de la requête écrite dans la variable CREATE_BDD
        for(SQLiteTable table : _tables)
        {
            sqLiteDatabase.execSQL(table.createStatement());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // later...
    }

    private Cursor query(SQLiteQuery query)
    {
        return this.getReadableDatabase().query(
                query.isDistinct(),
                query.getTableName(),
                query.getColumns(),
                query.getSelection(),
                query.getSelectionArgs(),
                query.getGroupBy(),
                query.getHaving(),
                query.getOrderBy(),
                query.getLimit()
        );
    }

    public <T extends SQLiteEntity> ArrayList<T> executeQuery(SQLiteQuery<T> query) {
        Cursor c = query(query);
        ArrayList<T> result = query.geTable().parseCursor(c);
        return result;
    }

    public <U extends SQLiteEntity> void createEntity(SQLiteTable<U> table, U item) {
        long rowID = this.getWritableDatabase().insert(table.name, null, table.serialize(item));
        item.set_zkey(rowID);
    }

    public <U extends SQLiteEntity> void updateEntity(SQLiteTable<U> table, U item) {
        String tableName = table.name;
        ContentValues values = table.serialize(item);
        String where = SQLiteQuery.Predicate.equal(SQLiteTable.PRIMARY_KEY_FIELD, item.get_zkey()).toString();
        this.getWritableDatabase().update(tableName, values, where, null);
    }

    public <U extends SQLiteEntity> void deleteEnetity(SQLiteTable<U> table, U item) {
        String tableName = table.name;
        String where = SQLiteQuery.Predicate.equal(SQLiteTable.PRIMARY_KEY_FIELD, item.get_zkey()).toString();
        this.getWritableDatabase().delete(tableName, where, null);
    }
}
