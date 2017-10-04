package f10.net.androidtoolbox.listAdapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import f10.net.androidtoolbox.asynclib.Handler;
import f10.net.androidtoolbox.asynclib.Promise;
import f10.net.androidtoolbox.binding.BaseFragment;

/**
 * Created by fl0 on 10/05/2017.
 */

public abstract class SmartListFragment<T> extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, SmartListAdapter.DataProvider<T>,SmartListAdapter.EventListener<T> {

    private SmartListAdapter<T>   adapter;
    private ListView              listView;
    private SwipeRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.refreshLayout = new SwipeRefreshLayout(getActivity());
        this.refreshLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        this.listView = new ListView(getActivity());
        this.listView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        this.refreshLayout.addView(this.listView);

        this.adapter = new SmartListAdapter<T>(this, this) {
            @Override
            protected int getCellResource() {
                return getCellLayout();
            }

            @Override
            protected void registerView(View cell, ViewHolder vh) {
                bindViews(cell, vh);
            }

            @Override
            protected void onBindViewHolder(ViewHolder vh, T item) {
                bindViewHolder(vh, item);
            }
        };

        this.listView.setAdapter(this.adapter);

        this.refreshLayout.setOnRefreshListener(this);

        return this.refreshLayout;
    }

    protected void setEmptyView(View view){
        this.listView.setEmptyView(view);
    }

    protected void setRefreshEnable(boolean enable)
    {
        this.refreshLayout.setEnabled(enable);
    }

    public final void onRefresh()
    {
        Promise<Boolean> p = new Promise<Boolean>();

        refreshDataSet(p);

        p.addSuccesHandler(new Handler<Boolean>() {
            @Override
            public void onFinished(Boolean result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });

        p.addErrorHandler(new Handler<Exception>() {
            @Override
            public void onFinished(final Exception result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), result.getMessage(), Toast.LENGTH_LONG).show();
                        refreshLayout.setRefreshing(false);
                    }
                });

            }
        });
    }

    public abstract int getCellLayout();

    public abstract void bindViews(View cell, ViewHolder vh);

    public abstract void bindViewHolder(ViewHolder vh, T item);

    protected abstract void refreshDataSet(Promise<Boolean> completion);

    @Override
    public abstract List<T> getElements();

    @Override
    public abstract void onSelect(T element);
}
