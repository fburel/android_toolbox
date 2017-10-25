package f10.net.androidtoolbox.views;

import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by fl0 on 16/05/2017.
 */

public class ImageSelectorSwitch implements View.OnClickListener {

    public int addChoice(ImageView view, int selectedImageResource, int unselectedImageResource)
    {
        this.imageViews.add(view);

        view.setOnClickListener(this);

        _selectedImageResouces.add(selectedImageResource);
        _unselectedImageResources.add(unselectedImageResource);

        return this.imageViews.size();
    }

    public void setSelectedIndex(int index)
    {
        selected = index;

        for(int i = 0; i < imageViews.size() ; i++)
        {
            ImageView iv = imageViews.get(i);
            if(i == selected)
            {
                iv.setImageResource(_selectedImageResouces.get(i));
            }
            else
            {
                iv.setImageResource(_unselectedImageResources.get(i));
            }
        }
    }

    public void setValueChangeListener(ImageSelectorSwitch.ValueChangedListener listener)
    {
        _listener = listener;
    }

    @Override
    public void onClick(View view) {

        int clicked = imageViews.indexOf(view);
        if(clicked != selected)
        {
            setSelectedIndex(clicked);
            if(_listener != null)
                _listener.onValueChanged(clicked);
        }
    }

    public interface ValueChangedListener {
        void onValueChanged(int selectedIndex);
    }

    private ArrayList<Integer> _unselectedImageResources = new ArrayList<Integer>();
    private ArrayList<Integer> _selectedImageResouces = new ArrayList<Integer>();
    private ValueChangedListener _listener;
    private ArrayList<ImageView> imageViews = new ArrayList<ImageView>();
    private int selected = 0;


}
