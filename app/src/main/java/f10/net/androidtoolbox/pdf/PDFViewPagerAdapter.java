package f10.net.androidtoolbox.pdf;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;

/**
 * Created by fl0 on 25/07/2017.
 */

public class PDFViewPagerAdapter extends PagerAdapter {

    public PDFViewPagerAdapter(Context context, PDFDocument pdfDocument) {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }
}
