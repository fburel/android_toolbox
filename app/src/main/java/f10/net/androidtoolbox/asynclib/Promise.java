package f10.net.androidtoolbox.asynclib;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeoutException;

/**
 * Created by fl0 on 15/03/2017.
 */

public class Promise<T> {

    private static class CountDownPromise extends Promise<Void>
    {
        int _value = 0;

        CountDownPromise(int startValue)
        {
            _value = startValue;
        }

        void decrement()
        {
            _value = _value - 1;
            if(_value <= 0)
            {
                succeeds(null);
            }
        }
    }

    private enum Status {
        PENDING,
        DONE,
        ERROR
    }

    private final ArrayList<Handler<T>> successHandlers = new ArrayList<Handler<T>>();
    private final ArrayList<Handler<Exception>> errorHandlers = new ArrayList<Handler<Exception>>();
    private T result;
    private Exception error;
    private Status status = Status.PENDING;

    public void addSuccesHandler(final Handler<T> handler){

        if(status == Status.DONE)
        {
            handler.onFinished(result);
        }
        else if(status == Status.PENDING)
        {
            successHandlers.add(handler);
        }
    }

    public void addErrorHandler(final Handler<Exception> handler){

        if(status == Status.ERROR)
        {
            handler.onFinished(error);
        }
        else if(status == Status.PENDING)
        {
            errorHandlers.add(handler);
        }
    }

    public void fails(final Exception error)
    {
        if(this.status != Status.PENDING){
            return;
        }

        this.status = Status.ERROR;
        this.error = error;

        for(final Handler<Exception> handler : this.errorHandlers){

            handler.onFinished(error);
        }
    }

    public void succeeds(final T result)
    {
        if(this.status != Status.PENDING){
            return;
        }
        this.status = Status.DONE;
        this.result = result;

        for(final Handler<T> handler : this.successHandlers){
            handler.onFinished(result);
        }
    }

    public void link(final Promise<T> follower)
    {
        this.addErrorHandler(new Handler<Exception>() {
            @Override
            public void onFinished(Exception result) {
                follower.fails(result);
            }
        });

        this.addSuccesHandler(new Handler<T>() {
            @Override
            public void onFinished(T result) {
                follower.succeeds(result);
            }
        });
    }

    public <U> void link(final Promise<U> follower, final Mapper<T, U> mapper)
    {
        this.addErrorHandler(new Handler<Exception>() {
            @Override
            public void onFinished(Exception result) {
                follower.fails(result);
            }
        });

        this.addSuccesHandler(new Handler<T>() {
            @Override
            public void onFinished(T result) {
                try {
                    follower.succeeds(mapper.map(result));
                } catch (Exception e) {
                    follower.fails(e);
                }
            }
        });
    }

    public static Promise<Void> whenAll(Promise[] promises)
    {
        return whenAll(promises, false);
    }

    public static Promise<Void> whenAll(Promise[] promises, boolean ignoreFailures)
    {
        final CountDownPromise whenAll = new CountDownPromise(promises.length);

        for(Promise p : promises)
        {
            p.addSuccesHandler(new Handler() {
                @Override
                public void onFinished(Object result) {
                    whenAll.decrement();
                }
            });
            if(ignoreFailures)
            {
                p.addErrorHandler(new Handler<Exception>() {
                    @Override
                    public void onFinished(Exception result) {
                        whenAll.decrement();
                    }
                });
            }
            else
            {
                p.addErrorHandler(new Handler<Exception>() {
                    @Override
                    public void onFinished(Exception result) {
                        whenAll.fails(result);
                    }
                });
            }

        }

        return whenAll;
    }

    public static <U> Promise<U> fromResult(U result)
    {
        Promise<U> p = new Promise<U>();
        p.succeeds(result);
        return p;
    }

    public static <U> Promise<U> fromResultAfterDelay(final U result, long milliseconds)
    {
        final Promise<U> p = new Promise<U>();

        new Timer().schedule(new TimerTask(){
            public void run() {
                p.succeeds(result);
            }
        }, milliseconds);

        return p;
    }


    public void setFailedAfterDelay(long delay) {
        Promise<Boolean> p = Promise.fromResultAfterDelay(Boolean.FALSE, delay);
        p.addSuccesHandler(new Handler<Boolean>() {
            @Override
            public void onFinished(Boolean result) {
                fails(new TimeoutException("TimeOut"));
            }
        });
    }


}
