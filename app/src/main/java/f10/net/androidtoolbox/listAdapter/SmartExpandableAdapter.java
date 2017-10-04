package f10.net.androidtoolbox.listAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import f10.net.androidtoolbox.R;


/**
 * Created by fl0 on 28/06/2017.
 */

public class SmartExpandableAdapter<T> extends BaseExpandableListAdapter {

    public static final String HEADER_DEFAULT_TEXT_VIEW = "Label";

    public interface SmartExpandableAdapterDataSource<T> {
        List<List<T>> getData();
        String getSectionTitle(int groupId);
        void onItemSelected(T item);
        int getElementCellResource();
        void onBindViewHolder(View cell, ViewHolder h);
        void onBindCell(ViewHolder holder, T child);
    }

    private final SmartExpandableAdapterDataSource<T> _dataSource;
    private final List<List<T>> _data;

    public SmartExpandableAdapter(SmartExpandableAdapterDataSource<T> dataSource) {
        _dataSource = dataSource;
        _data = dataSource.getData();
    }

    @Override
    public int getGroupCount() {
        return _data.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return _data.get(i).size();
    }

    @Override
    public Object getGroup(int i) {
        return _data.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return _data.get(i).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i * 10000 + i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View convertView, ViewGroup parent) {

        View cell = convertView;

        if (cell == null) {
            cell = LayoutInflater.from(parent.getContext()).inflate(getSectionHeaderCell(), null);
            ViewHolder vh = new ViewHolder();
            onBindHeaderViewHolder(vh, cell);
            cell.setTag(vh);
        }

        ViewHolder holder = (ViewHolder) cell.getTag();


        onCreateHeader(holder, i);

        return cell;
    }

    @Override
    public View getChildView(int groupPosition, int itemPosition, boolean b, View convertView, ViewGroup parent) {

        T child = (T) getChild(groupPosition, itemPosition);

        View cell = convertView;

        if (cell == null) {

            cell = LayoutInflater.from(parent.getContext()).inflate(_dataSource.getElementCellResource(), null);
            ViewHolder h = new ViewHolder(cell, this);
            _dataSource.onBindViewHolder(cell, h);
            cell.setTag(h);
        }

        ViewHolder holder = (ViewHolder) cell.getTag();;
        holder.position = new IndexPath(groupPosition, itemPosition);
        _dataSource.onBindCell(holder, child);
        return cell;
    }

    public static class IndexPath {
        public final int section;
        public final int position;

        public IndexPath(int section, int position) {
            this.section = section;
            this.position = position;
        }
    }

    public static class ViewHolder implements View.OnClickListener {

        private IndexPath position;
        private final HashMap<String, View> views;
        private final SmartExpandableAdapter callback;

        public ViewHolder() {
            views = new HashMap<String, View>();
            callback = null;
        }

        public ViewHolder(View cell, SmartExpandableAdapter adapterCallback) {
            views = new HashMap<String, View>();
            cell.setOnClickListener(this);
            callback = adapterCallback;
        }

        public void registerView(String label, View v)
        {
            views.put(label, v);
        }

        public View getView(String label) {
            return views.get(label);
        }

        @Override
        public void onClick(View view) {
            if(callback != null) {
                callback.onClick(position, view);
            }
        }
    }


    // base method -- can be ovverriden --

    protected void onClick(IndexPath position, View view) {
        T child = (T) getChild(position.section, position.position);
        _dataSource.onItemSelected(child);
    }

    protected int getSectionHeaderCell() {
        return R.layout.expandable_list_section_header;
    }

    protected void onBindHeaderViewHolder(ViewHolder vh, View cell) {
        vh.registerView(HEADER_DEFAULT_TEXT_VIEW, cell.findViewById(android.R.id.text1));
    }

    protected void onCreateHeader(ViewHolder holder, int groupId)
    {
        String title = _dataSource.getSectionTitle(groupId);
        ((TextView)holder.getView(HEADER_DEFAULT_TEXT_VIEW)).setText(title);
    }
}
