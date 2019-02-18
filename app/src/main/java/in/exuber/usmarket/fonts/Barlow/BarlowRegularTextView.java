package in.exuber.usmarket.fonts.Barlow;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;


public class BarlowRegularTextView extends android.support.v7.widget.AppCompatTextView {

    public BarlowRegularTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public BarlowRegularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BarlowRegularTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Barlow-Regular.ttf");
            setTypeface(tf);
        }
    }

}