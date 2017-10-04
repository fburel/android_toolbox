package f10.net.androidtoolbox.asynclib;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Looper;
import android.support.annotation.NonNull;


/**
 * Created by fl0 on 06/04/2017.
 */

public class PromiseWaitingDialog<T> extends ProgressDialog {


    public static <T> void show(@NonNull Context context, Promise<T> t)
    {
        show(context, t, "Please wait");
    }

    public static <T> void show(@NonNull Context context, Promise<T> t, String message)
    {
        PromiseWaitingDialog<T> dlg = new PromiseWaitingDialog<T>(context, t, message);
        dlg.show();
    }

    private PromiseWaitingDialog(@NonNull Context context, Promise<T> t, String message) {

        super(context);

        t.addSuccesHandler(new Handler<T>() {
            @Override
            public void onFinished(T result) {
                new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        dismiss();
                    }
                });
            }
        });

        t.addErrorHandler(new Handler<Exception>() {
            @Override
            public void onFinished(Exception result) {
                dismiss();
            }
        });

        setProgressStyle(ProgressDialog.STYLE_SPINNER);

        setMessage(message);

    }


}
