package f10.net.androidtoolboxdemo.Segue;

import android.os.Bundle;

public enum SegueEvents {

    CitySelected;

    private Bundle bundle = new Bundle();

    public SegueEvents addBundle(Bundle b)
    {
        bundle = b;
        return this;
    }

    public Bundle getBundle() {
        return bundle;
    }
}
