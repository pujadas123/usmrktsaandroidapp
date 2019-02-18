package in.exuber.usmarket.fonts.Barlow;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;


public class BarlowMediumTextView extends android.support.v7.widget.AppCompatTextView {

    public BarlowMediumTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public BarlowMediumTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BarlowMediumTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Barlow-Medium.ttf");
            setTypeface(tf);
        }
    }

}