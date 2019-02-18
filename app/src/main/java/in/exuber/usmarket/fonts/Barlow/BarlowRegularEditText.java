package in.exuber.usmarket.fonts.Barlow;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by one on 3/12/15.
 */
public class BarlowRegularEditText extends android.support.v7.widget.AppCompatEditText {

    public BarlowRegularEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public BarlowRegularEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BarlowRegularEditText(Context context) {
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