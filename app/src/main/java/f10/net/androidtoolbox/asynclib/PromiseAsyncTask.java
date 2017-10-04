package f10.net.androidtoolbox.asynclib;

import android.os.AsyncTask;


/**
 * Created by fl0 on 05/04/2017.
 */

public class PromiseAsyncTask<T> {

    private class CompleteAsyncTask<T> extends AsyncTask<Object, Object, Object>
    {
        final T r;
        final Exception e;
        final Promise<T> p;

        public CompleteAsyncTask(T result, Promise<T> linkedPromise) {
            r = result;
            e = null;
            p = linkedPromise;
        }

        public CompleteAsyncTask(Exception exception, Promise<T> linkedPromise) {
            r = null;
            e = exception;
            p = linkedPromise;
        }

        @Override
        protected Object doInBackground(Object... objects) {
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if(e != null)
            {
                p.fails(e);
            }
            else
            {
                p.succeeds(r);
            }
        }
    }

    private class ExecuteAsyncTask<T> extends AsyncTask<Object, Object, Promise<T>>
    {
        final Task<T> task;
        final Promise<T> promise;

        public ExecuteAsyncTask(Task<T> task, Promise<T> promise) {
            this.task = task;
            this.promise = promise;
        }

        @Override
        protected Promise<T> doInBackground(Object... voids) {

            final Promise<T> p = new Promise<T>();

            task.perform(p);

            p.addSuccesHandler(new Handler<T>() {
                @Override
                public void onFinished(T result) {
                    new CompleteAsyncTask<T>(result, promise).execute();
                }
            });

            p.addErrorHandler(new Handler<Exception>() {
                @Override
                public void onFinished(Exception result) {
                    new CompleteAsyncTask<T>(result, promise).execute();
                }
            });

            return null;
        }
    }


    private final Promise<T> promise;
    private final ExecuteAsyncTask<T> asyncTask;

    private PromiseAsyncTask(Task<T> task) {
        promise = new Promise<T>();
        this.asyncTask = new ExecuteAsyncTask<T>(task, promise);
    }

    private Promise<T> getPromise() {
        return promise;
    }

    public static <T> Promise<T> from(Task<T> t)
    {
        PromiseAsyncTask<T> pat = new PromiseAsyncTask<T>(t);
        pat.asyncTask.execute();
        return pat.getPromise();
    }

    public static <U, T> Promise<T> chain(Promise<U> first, Task<T> followActionIfSuccess)
    {
        final PromiseAsyncTask<T> pat = new PromiseAsyncTask<T>(followActionIfSuccess);

        first.addSuccesHandler(new Handler<U>() {
            @Override
            public void onFinished(U result) {
                pat.asyncTask.execute();
            }
        });

        first.addErrorHandler(new Handler<Exception>() {
            @Override
            public void onFinished(Exception result) {
                pat.promise.fails(result);
            }
        });

        return pat.promise;

    }

    public static <T> Promise<T> afterDelay(Task<T> t, final int milliseconds)
    {
        Promise<Boolean> delay = Promise.fromResultAfterDelay(Boolean.FALSE, milliseconds);

        return chain(delay, t);
    }

}