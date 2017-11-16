package f10.net.androidtoolbox.navigation;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;

import f10.net.androidtoolbox.R;


/**
 * Created by fl0 on 05/05/2017.
 */

/**
 * extends Drawer activity to provide push / pop (master-detail) navigation between a succession of fragment.
 */
public abstract class PushPopActivity extends DrawerActivity {

    private final ArrayList<Fragment> _fragments = new ArrayList<Fragment>();

    @Override
    protected View onCreateMainView(Bundle savedInstanceState) {

        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setId(R.id.drawerActivityFragmentLayout);
        frameLayout.setTag("fragmentPlaceholder");

        return frameLayout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);

        askForPermissionsIfNeeded();

        // Load 1st fragment if needed

        if (_fragments.size() == 0)
            reset();

    }

    /** Navigation **/

    protected void push(Fragment f, Bundle extra) {
        f.setArguments(extra);
        push(f);
    }

    protected void push(Fragment f)
    {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.push_entry, R.anim.push_exit)
                .replace(R.id.drawerActivityFragmentLayout, f)
                .commit();


        _fragments.add(f);
    }

    protected void pop()
    {

        _fragments.remove(_fragments.size() - 1);
        Fragment previous = _fragments.get(_fragments.size() - 1);

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.pop_entry, R.anim.pop_exit)
                .replace(R.id.drawerActivityFragmentLayout, previous)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if(_fragments.size() > 1)
        {
            pop();
        }
        else
        {
            onActivityBack();
        }
    }

    protected void onActivityBack() {
        super.onBackPressed();
    }

    /**
     * override to request your own permission at runtime
     */
    protected abstract void askForPermissionsIfNeeded();

    public abstract Class<? extends Fragment> getStartActivityClass();

    public Fragment reset()
    {
        _fragments.clear();

        Fragment f = null;

        try {
            f = getStartActivityClass().newInstance();
        } catch (Exception e) {
            f = new Fragment();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.drawerActivityFragmentLayout, f)
                .commit();


        _fragments.add(f);

        return f;
    }

    public void presentModally(Class<? extends Fragment> fragment)
    {
        presentModally(fragment, new Bundle());
    }

    public void presentModally(Class<? extends Fragment> fragment, Bundle extras)
    {
        Intent intent = new Intent(this, ModalActivity.class);
        intent.putExtra(ModalActivity.MODAL_FRAGMENT, fragment.getName());
        intent.putExtra(ModalActivity.MODAL_EXTRAS, extras);
        startActivity(intent);
        overridePendingTransition(R.anim.modal_anim_entry, R.anim.stay_put);
    }

    public void dismissModalActivity()
    {
        ModalActivity.ModalActivityManager.dismiss();
    }
}
