package f10.net.androidtoolbox.sqlite;

/**
 * Created by fl0 on 09/05/2017.
 */

public class SQLiteQuery<T extends SQLiteEntity> {

    private final SQLiteTable<T> mTable;
    private boolean distinct = false;
    private final String table;
    private final String[] columns;
    private String selection = null;
    private String[] selectionArgs = null;
    private String groupBy = null;
    private String having = null;
    private String orderBy = null;
    private String limit = null;

    SQLiteTable<T> geTable() {
        return mTable;
    }

    boolean isDistinct() {
        return distinct;
    }

    String getTableName() {
        return table;
    }

    String[] getColumns() {
        return columns;
    }

    String getSelection() {
        return selection;
    }

    String[] getSelectionArgs() {
        return selectionArgs;
    }

    String getGroupBy() {
        return groupBy;
    }

    String getHaving() {
        return having;
    }

    String getOrderBy() {
        return orderBy;
    }

    String getLimit() {
        return limit;
    }



    public static <U extends SQLiteEntity> SQLiteQuery<U> selectFrom(SQLiteTable<U> table)
    {
        return new SQLiteQuery<U>(table);
    }
    private SQLiteQuery(SQLiteTable<T> table) {
        mTable = table;
        this.table = table.name;
        this.columns = (String[]) table.columns.toArray(new String[table.columns.size()]);
    }

    public SQLiteQuery<T> where(Predicate predicate) {
        selection = predicate.toString();
        return this;
    }

    public SQLiteQuery orderBy(String sortDescriptor) {
        orderBy = sortDescriptor;
        return this;
    }

    public static class Predicate{

        private final String _value;

        private Predicate(String value) {
            _value = value;
        }

        @Override
        public String toString() {
            return _value;
        }


        public static Predicate equal(String field, Number value) {
            return new Predicate(String.format("%s = %s", field, value.toString()));
        }

        public static Predicate equal(String field, String value) {
            return new Predicate(String.format("%s = \'%s\'", field, value));
        }

        public static Predicate notEqual(String field, Number value) {
            return new Predicate(String.format("%s <> %s", field, value.toString()));
        }

        public static Predicate notEqual(String field, String value) {
            return new Predicate(String.format("%s <> \'%s\'", field, value));
        }

        public static Predicate lessThan(String field, Number value) {
            return new Predicate(String.format("%s < %s", field, value.toString()));
        }

        public static Predicate lessThanOrEqual(String field, Number value) {
            return new Predicate(String.format("%s <= %s", field, value.toString()));
        }

        public static Predicate greaterThan(String field, Number value) {
            return new Predicate(String.format("%s > %s", field, value.toString()));
        }

        public static Predicate greaterThanOrEqual(String field, Number value) {
            return new Predicate(String.format("%s >= %s", field, value.toString()));
        }

        public static Predicate in(String field, Number[] values) {
            StringBuilder builder = new StringBuilder();
            builder.append("(");
            for (Number p : values){
                builder.append(String.format("%s,", p));
            }
            builder.delete(builder.length() - 1, 1); // remove the last coma
            builder.append(")");
            return new Predicate(String.format("%s IN %s", field, builder.toString()));
        }

        public static Predicate in(String field, String[] values) {
            StringBuilder builder = new StringBuilder();
            builder.append("(");
            for (String p : values){
                builder.append(String.format("\'%s\',", p));
            }
            builder.delete(builder.length() - 1, 1); // remove the last coma
            builder.append(")");
            return new Predicate(String.format("%s IN %s", field, builder.toString()));
        }

        public static Predicate between(String field, Number lowLimit, Number highLimit) {
            return new Predicate(String.format("%s BETWEEN %s AND %s", field, lowLimit, highLimit));
        }

        public static Predicate like(String field, String pattern) {
            return new Predicate(String.format("%s LIKE \'%s\'", field, pattern));
        }

        public static Predicate and(Predicate p1, Predicate p2){
            return new Predicate(String.format("(%s AND %s)", p1.toString(), p2.toString()));
        }

        public static Predicate or(Predicate p1, Predicate p2){
            return new Predicate(String.format("(%s OR %s)", p1.toString(), p2.toString()));
        }

        public static Predicate not(Predicate p1){
            return new Predicate(String.format("NOT %s", p1.toString()));
        }
    }
}