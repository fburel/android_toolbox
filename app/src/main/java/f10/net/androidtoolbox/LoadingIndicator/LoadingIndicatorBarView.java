package f10.net.androidtoolbox.LoadingIndicator;

import android.content.Context;
import android.graphics.Color;
import android.widget.RelativeLayout;

import f10.net.androidtoolbox.graphics.RoundedCorner;

/**
 * Created by fl0 on 28/07/2017.
 */

public class LoadingIndicatorBarView extends RelativeLayout
{
    private Context context;
    private float cornerRadius;

    public LoadingIndicatorBarView(Context context, float cornerRadius)
    {
        super(context);

        this.context = context;
        this.cornerRadius = cornerRadius;

        initViews();
    }

    public void initViews()
    {
        setBackground(RoundedCorner.roundedCornerRectWithColor(Color.argb(255, 255, 255, 255), cornerRadius));

        setAlpha(0.5f);
    }

    public void resetColor()
    {
        setBackground(RoundedCorner.roundedCornerRectWithColor(Color.argb(255, 255, 255, 255), cornerRadius));

        setAlpha(0.5f);
    }
}