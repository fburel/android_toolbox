package f10.net.androidtoolbox.forms;

/**
 * Created by fl0 on 25/07/2017.
 */

public class StringValueCell extends ValueCell<String> {

    public StringValueCell(int tag, FormFragment form, String label, String value) {
        super(tag, form, label, value);
    }

    @Override
    protected String onUpdatingValueText(String value) {
        return value;
    }
}
