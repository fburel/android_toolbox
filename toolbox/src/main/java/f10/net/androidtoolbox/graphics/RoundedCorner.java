package f10.net.androidtoolbox.graphics;

import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;

/**
 * Created by fl0 on 28/07/2017.
 */

public class RoundedCorner {

    public static ShapeDrawable roundedCornerRectOutlineWithColor(int color, float cornerRadius,
                                                                  float strokeWidth)
    {
        float[] radii = new float[] {
                cornerRadius, cornerRadius,
                cornerRadius, cornerRadius,
                cornerRadius, cornerRadius,
                cornerRadius, cornerRadius
        };

        RoundRectShape roundedCornerShape = new RoundRectShape(radii, null, null);

        ShapeDrawable shape = new ShapeDrawable();
        shape.getPaint().setColor(color);
        shape.setShape(roundedCornerShape);
        shape.getPaint().setStrokeWidth(strokeWidth);
        shape.getPaint().setStyle(Paint.Style.STROKE);

        return shape;
    }

    public static ShapeDrawable roundedCornerRectWithColor(int color, float cornerRadius)
    {
        float[] radii = new float[] {
                cornerRadius, cornerRadius,
                cornerRadius, cornerRadius,
                cornerRadius, cornerRadius,
                cornerRadius, cornerRadius
        };

        RoundRectShape roundedCornerShape = new RoundRectShape(radii, null, null);

        ShapeDrawable shape = new ShapeDrawable();
        shape.getPaint().setColor(color);
        shape.setShape(roundedCornerShape);

        return shape;
    }

    public static ShapeDrawable roundedCornerRectWithColor(int color, float topLeftRadius, float
            topRightRadius, float bottomRightRadius, float bottomLeftRadius)
    {
        float[] radii = new float[] {
                topLeftRadius, topLeftRadius,
                topRightRadius, topRightRadius,
                bottomRightRadius, bottomRightRadius,
                bottomLeftRadius, bottomLeftRadius
        };

        RoundRectShape roundedCornerShape = new RoundRectShape(radii, null, null);

        ShapeDrawable shape = new ShapeDrawable();
        shape.getPaint().setColor(color);
        shape.setShape(roundedCornerShape);

        return shape;
    }

}
