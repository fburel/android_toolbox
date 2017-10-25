package f10.net.androidtoolbox.navigation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import f10.net.androidtoolbox.R;

/**
 * Created by fl0 on 10/05/2017.
 */

public class ModalActivity extends PushPopActivity {

    public static final String MODAL_EXTRAS = "fr.florianburel.toolbox.navigation.modalactivity.modalextras";

    public static class ModalActivityManager
    {
        private final static ArrayList<ModalActivity> Activities = new ArrayList<ModalActivity>();
        private static void registerCurrent(ModalActivity activity) {
            clearReference(activity);
            Activities.add(activity);
        }

        private static void clearReference(ModalActivity modalActivity) {
            Activities.remove(modalActivity);
        }

        private static ModalActivity getCurrentModalActivity() {
            return Activities.get(Activities.size() -1);
        }


        public static void dismiss()
        {
            getCurrentModalActivity().dismiss();
        }
    }



    public static final String MODAL_FRAGMENT = "fr.florianburel.toolbox.uitools.modalactivity.modalfragment";

    @Override
    public Class<? extends Fragment> getStartActivityClass() {

        Bundle extras = getIntent().getExtras();
        String fragmentClass;

        if(extras == null) {
            fragmentClass= null;
        } else {
            fragmentClass = extras.getString(MODAL_FRAGMENT);
        }

        Class<? extends Fragment> fragment = null;

        try {
            fragment = (Class<? extends Fragment>) Class.forName(getIntent().getStringExtra(MODAL_FRAGMENT));
        } catch (Exception e) {
            e.printStackTrace();
        }

        assert fragment != null;

        return fragment;
    }

    @Override
    public Fragment reset() {
        Fragment f = super.reset();
        if(getIntent().getExtras() != null)
        {
            f.setArguments(getIntent().getExtras().getBundle(MODAL_EXTRAS));
        }
        return f;
    }

    @Override
    protected boolean prepareSideMenu(Menu menu) {
        return false;
    }

    @Override
    protected void onSideMenuItemSelected(MenuItem element) {
        // do nothing...
    }

    @Override
    protected void askForPermissionsIfNeeded() {

    }

    protected void onResume() {
        super.onResume();
        ModalActivityManager.registerCurrent(this);
    }
    protected void onPause() {
        ModalActivityManager.clearReference(this);
        super.onPause();
    }
    protected void onDestroy() {
        ModalActivityManager.clearReference(this);
        super.onDestroy();
    }

    private void dismiss() {
        finish();
        overridePendingTransition(R.anim.stay_put, R.anim.modal_anim_exit);
    }

    @Override
    protected void onActivityBack() {
        dismiss();
    }
}
