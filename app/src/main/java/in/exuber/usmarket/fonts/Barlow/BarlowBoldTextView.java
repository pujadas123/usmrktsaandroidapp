package in.exuber.usmarket.fonts.Barlow;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;


public class BarlowBoldTextView extends android.support.v7.widget.AppCompatTextView {

    public BarlowBoldTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public BarlowBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BarlowBoldTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Barlow-Bold.ttf");
            setTypeface(tf);
        }
    }

}