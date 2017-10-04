package f10.net.androidtoolbox.binding;


import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import f10.net.androidtoolbox.R;


/**
 * Created by fl0 on 27/06/2017.
 */

public class BaseFragment extends Fragment {


    private class MenuButton {
        private final int tag;
        private final int icon;
        private final String title;

        private MenuButton(int tag, int icon, String title) {
            this.tag = tag;
            this.icon = icon;
            this.title = title;
        }
    }

    private MenuButton _rightMenuButton = null;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        menu.clear();

        menu.add(Menu.NONE, _rightMenuButton.tag, Menu.NONE, _rightMenuButton.title)
                .setIcon(_rightMenuButton.icon)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

    }

    @Override
    public void onStart() {
        super.onStart();
        if(this.getTitle() != null) {
            this.getActivity().setTitle(this.getTitle());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.right_menu_button)
        {
            onRightButtonItemClicked();
        }

        return true;
    }

    protected void setRightButtonItem(String title, int icon)
    {
        setHasOptionsMenu(true);
        _rightMenuButton = new MenuButton(R.id.right_menu_button, icon, title);
    }

    protected void onRightButtonItemClicked()
    {

    }

    public String getTitle() {
        return null;
    }

}
