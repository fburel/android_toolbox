package f10.net.androidtoolbox.graphics;

import android.content.res.ColorStateList;

import java.util.Arrays;

/**
 * Created by fl0 on 23/06/2017.
 */

public class ColorStateListBuilder {

    private int[][] _states = new int[0][];
    private int[] _colors = new int[0];

    public ColorStateListBuilder addColorForState(int state, int color)
    {
        _colors = Arrays.copyOf(_colors, _colors.length + 1);
        _colors[_colors.length - 1] = color;

        _states = Arrays.copyOf(_states, _states.length + 1);
        _states[_states.length - 1] = new int[]{state};
        return this;
    }

    public ColorStateList build()
    {
        return new ColorStateList(_states, _colors);
    }
}

