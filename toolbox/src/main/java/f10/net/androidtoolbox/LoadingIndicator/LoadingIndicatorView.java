package f10.net.androidtoolbox.LoadingIndicator;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Created by fl0 on 28/07/2017.
 */

public class LoadingIndicatorView extends RelativeLayout
{
    private Context context;

    private int numberOfBars;

    public ArrayList<LoadingIndicatorBarView> arrBars;

    public float radius;

    private boolean isAnimating;
    private int currentFrame;

    private final Handler handler = new Handler();
    private Runnable playFrameRunnable;

    public LoadingIndicatorView(Context context, float radius)
    {
        super(context);

        this.context = context;
        this.radius = radius;
        this.numberOfBars = 12;

        initViews();
        initLayouts();
        addViews();
        spreadBars();
    }

    public void initViews()
    {
        arrBars = new ArrayList<LoadingIndicatorBarView>();

        for(int i = 0; i < numberOfBars; i++)
        {
            LoadingIndicatorBarView bar = new LoadingIndicatorBarView(context, radius / 10.0f);

            arrBars.add(bar);
        }
    }

    public void initLayouts()
    {
        for(int i = 0; i < numberOfBars; i++)
        {
            LoadingIndicatorBarView bar = arrBars.get(i);

            bar.setId(View.generateViewId());

            LayoutParams barLayoutParams = new LayoutParams(
                    (int)(radius / 5.0f),
                    (int)(radius / 2.0f)
            );

            barLayoutParams.addRule(ALIGN_PARENT_TOP);
            barLayoutParams.addRule(CENTER_HORIZONTAL);

            bar.setLayoutParams(barLayoutParams);
        }
    }

    public void addViews()
    {
        for(int i = 0; i < numberOfBars; i++)
        {
            LoadingIndicatorBarView bar = arrBars.get(i);

            addView(bar);
        }
    }

    public void spreadBars()
    {
        int degrees = 0;

        for(int i = 0; i < arrBars.size(); i++)
        {
            LoadingIndicatorBarView bar = arrBars.get(i);

            rotateBar(bar, degrees);

            degrees += 30;
        }
    }

    private void rotateBar(LoadingIndicatorBarView bar, float degrees)
    {
        RotateAnimation animation = new RotateAnimation(0, degrees, radius / 10.0f, radius);
        animation.setDuration(0);
        animation.setFillAfter(true);

        bar.setAnimation(animation);
        animation.start();
    }

    public void startAnimating()
    {
        setAlpha(1.0f);

        isAnimating = true;

        playFrameRunnable = new Runnable()
        {
            @Override
            public void run()
            {
                playFrame();
            }
        };

        // recursive function until isAnimating is false
        playFrame();
    }

    public void stopAnimating()
    {
        isAnimating = false;

        setAlpha(0.0f);

        invalidate();

        playFrameRunnable = null;
    }

    private void playFrame()
    {
        if(isAnimating)
        {
            resetAllBarAlpha();
            updateFrame();

            handler.postDelayed(playFrameRunnable, 0);
        }
    }

    private void updateFrame()
    {
        if (isAnimating)
        {
            showFrame(currentFrame);
            currentFrame += 1;

            if (currentFrame > 11)
            {
                currentFrame = 0;
            }
        }
    }

    private void resetAllBarAlpha()
    {
        LoadingIndicatorBarView bar = null;

        for (int i = 0; i < arrBars.size(); i++)
        {
            bar = arrBars.get(i);

            bar.setAlpha(0.5f);
        }
    }

    private void showFrame(int frameNumber)
    {
        int[] indexes = getFrameIndexesForFrameNumber(frameNumber);

        gradientColorBarSets(indexes);
    }

    private int[] getFrameIndexesForFrameNumber(int frameNumber)
    {
        if(frameNumber == 0)
        {
            return indexesFromNumbers(0, 11, 10, 9);
        }
        else if(frameNumber == 1)
        {
            return indexesFromNumbers(1, 0, 11, 10);
        }
        else if(frameNumber == 2)
        {
            return indexesFromNumbers(2, 1, 0, 11);
        }
        else if(frameNumber == 3)
        {
            return indexesFromNumbers(3, 2, 1, 0);
        }
        else if(frameNumber == 4)
        {
            return indexesFromNumbers(4, 3, 2, 1);
        }
        else if(frameNumber == 5)
        {
            return indexesFromNumbers(5, 4, 3, 2);
        }
        else if(frameNumber == 6)
        {
            return indexesFromNumbers(6, 5, 4, 3);
        }
        else if(frameNumber == 7)
        {
            return indexesFromNumbers(7, 6, 5, 4);
        }
        else if(frameNumber == 8)
        {
            return indexesFromNumbers(8, 7, 6, 5);
        }
        else if(frameNumber == 9)
        {
            return indexesFromNumbers(9, 8, 7, 6);
        }
        else if(frameNumber == 10)
        {
            return indexesFromNumbers(10, 9, 8, 7);
        }
        else
        {
            return indexesFromNumbers(11, 10, 9, 8);
        }
    }

    private int[] indexesFromNumbers(int i1, int i2, int i3, int i4)
    {
        int[] indexes = {i1, i2, i3, i4};
        return indexes;
    }

    private void gradientColorBarSets(int[] indexes)
    {
        float alpha = 1.0f;

        LoadingIndicatorBarView barView = null;

        for(int i = 0; i < indexes.length; i++)
        {
            int barIndex = indexes[i];

            barView = arrBars.get(barIndex);


            barView.setAlpha(alpha);
            alpha -= 0.125f;
        }

        invalidate();
    }
}