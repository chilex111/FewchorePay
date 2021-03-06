package fewchore.com.exolve.fewchore.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import fewchore.com.exolve.fewchore.R;


/**
 * PoppinsButton by Ekerete, created on 29/01/2018
 **/
public class PoppinsButton extends AppCompatButton {

//	private static final String TAG = "EditText";

    private Typeface typeface;

    public PoppinsButton(Context context) {
        super(context);
    }

    public PoppinsButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public PoppinsButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.PoppinsButton);
        String customFont = a.getString(R.styleable.PoppinsButton_PoppinsButton);
        setCustomFont(ctx, customFont);
        a.recycle();
    }

    private boolean setCustomFont(Context ctx, String asset) {
        try {
            if (typeface == null) {
                // Log.i(TAG, "asset:: " + "fonts/" + asset);
                typeface = Typeface.createFromAsset(ctx.getAssets(),
                        "Poppins-Regular.ttf");
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Log.e(TAG, "Could not get typeface: " + e.getMessage());
            return false;
        }

        setTypeface(typeface);
        return true;
    }
}
