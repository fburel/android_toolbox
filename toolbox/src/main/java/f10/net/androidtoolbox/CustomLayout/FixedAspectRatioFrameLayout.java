package f10.net.androidtoolbox.CustomLayout;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.FrameLayout;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class FixedAspectRatioFrameLayout extends FrameLayout{

    public FixedAspectRatioFrameLayout(@NonNull Context context) {
        super(context, null, 0, 0);
    }

    public FixedAspectRatioFrameLayout(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs, 0, 0);
    }

    public FixedAspectRatioFrameLayout(@NonNull Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, 0);
    }

    public FixedAspectRatioFrameLayout(@NonNull Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int minValue = Math.min(MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.getSize(widthMeasureSpec));
        super.onMeasure( MeasureSpec.makeMeasureSpec(minValue,MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(minValue,MeasureSpec.EXACTLY));
    }
}
