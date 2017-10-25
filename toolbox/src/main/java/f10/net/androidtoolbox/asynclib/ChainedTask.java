package f10.net.androidtoolbox.asynclib;

import android.support.design.widget.TabLayout;

/**
 * Created by fl0 on 18/10/2017.
 */

public interface ChainedTask<I, O> {

    void perform(I previousTaskResult, Promise<O> completion);
}

/*

private final Promise<I> _parent;

    public ChainedTask(Promise<I> parent) {
        _parent = parent;
    }

    @Override
    public void perform(final Promise<O> promise) {

        _parent.addSuccesHandler(new Handler<I>() {
            @Override
            public void onFinished(I result) {
                perform(result).link(promise);
            }
        });


 */