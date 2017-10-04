package f10.net.androidtoolbox.hud;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.ImageView;
import android.widget.TextView;

import f10.net.androidtoolbox.asynclib.Handler;
import f10.net.androidtoolbox.asynclib.Promise;
import f10.net.androidtoolbox.R;


/**
 * Created by fl0 on 05/06/2017.
 */

public class HUD {

    public static int DURATION_DEFAULT = 2000;

    public static void showError(Context context, String text, int duration)
    {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.loading_image);
        ImageView imageView = (ImageView) dialog.findViewById(R.id.loadingImage);
        TextView textView = (TextView) dialog.findViewById(R.id.textViewStatus);
        textView.setText(text);
        imageView.setImageResource(R.drawable.ic_errorstatus);
        dialog.show();

        Promise.fromResultAfterDelay(true, duration).addSuccesHandler(new Handler<Boolean>() {
            @Override
            public void onFinished(Boolean result) {
                dialog.dismiss();
            }
        });
    }

    public static void showSuccess(Context context, String text, int duration)
    {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.loading_image);
        ImageView imageView = (ImageView) dialog.findViewById(R.id.loadingImage);
        TextView textView = (TextView) dialog.findViewById(R.id.textViewStatus);
        textView.setText(text);
        imageView.setImageResource(R.drawable.ic_successstatus);
        dialog.show();

        Promise.fromResultAfterDelay(true, duration).addSuccesHandler(new Handler<Boolean>() {
            @Override
            public void onFinished(Boolean result) {
                dialog.dismiss();
            }
        });
    }
}
