package f10.net.androidtoolbox.forms;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.HashMap;

import f10.net.androidtoolbox.binding.BaseFragment;


/**
 * Created by fl0 on 11/05/2017.
 */

public abstract class FormFragment extends BaseFragment implements View.OnClickListener {

    ScrollView _scrollview;
    LinearLayout _contentLayout;

    HashMap<View, Cell> _cellMap = new HashMap<View, Cell>();
    Cell _focusedCell;



    protected final static int DEFAULT_ROW_HEIGHT = 80;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        _scrollview = new ScrollView(getActivity());
        _scrollview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


        _contentLayout = new LinearLayout(getActivity());
        _contentLayout.setOrientation(LinearLayout.VERTICAL);
        _contentLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        _scrollview.addView(_contentLayout);

        configureForm();

        return _scrollview;
    }


    protected ViewGroup addRow(Cell cell)
    {
        View v = cell.getView(getActivity());
        _cellMap.put(v, cell);
        v.setOnClickListener(this);

        LinearLayout l = new LinearLayout(getActivity());
        l.setOrientation(LinearLayout.VERTICAL);
        l.addView(v, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, getRowHeight(cell.getTag()), getResources().getDisplayMetrics())));
        l.addView(new View(this.getActivity()), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, getCellSeparatorGap(), getResources().getDisplayMetrics())));
        _contentLayout.addView(l);

        return l;

    }

    protected float getCellSeparatorGap() {
        return (float) 4.0;
    }


    protected void insertSectionHeader(String title) {
        _contentLayout.addView(onCreateSectionHeader(getActivity(), title),new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, toPixel(getSectionHeaderHeight())));
    }

    private int getSectionHeaderHeight() {
        return 44;
    }

    protected View onCreateSectionHeader(Context context, String title) {
        View view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, null);
        TextView tv = (TextView) view.findViewById(android.R.id.text1);
        tv.setText(title);
        tv.setAllCaps(true);
        tv.setTextColor(Color.LTGRAY);
        return view;
    }

    protected void insertSectionNote(String text)
    {
        TextView tv = new TextView(getActivity());
        tv.setText(text);
        tv.setTextSize(16);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);

        LinearLayout v = new LinearLayout(this.getActivity());
        v.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams center = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        center.setMargins(toPixel(8), toPixel(8), toPixel(8), toPixel(22));
        v.addView(tv, center);

        _contentLayout.addView(v);

    }

    public abstract void  configureForm();

    public abstract void onCellDataChanged(Cell cell, Object newValue);

    @Override
    public void onClick(View view) {

        _focusedCell = _cellMap.get(view);

        for(Cell cell : _cellMap.values())
        {
            if(cell != _focusedCell)
                cell.onLoosingFocus(getActivity());
        }

        _focusedCell.onGainingFocus(getActivity());
    }

    protected int getRowHeight(int cellTag)
    {
        return DEFAULT_ROW_HEIGHT;
    }

    protected void reload()
    {
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }

    int toPixel(int dp)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
