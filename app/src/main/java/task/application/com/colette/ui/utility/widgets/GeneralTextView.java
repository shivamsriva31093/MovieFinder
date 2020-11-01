package task.application.com.colette.ui.utility.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import task.application.com.colette.R;
import task.application.com.colette.util.FontCache;

/**
 * GeneralTextView is a custom textview that supports custom font. To use just set app:fontName="Font name".
 * Font name is like "Robota" or "Righteous" for Robota.tff/Righteous.tff
 * For great experience please use a font that supports different textstyles.
 * Otherwise on not finding the intended style
 * the system uses the default typeface.
 */
public class GeneralTextView extends androidx.appcompat.widget.AppCompatTextView {


    private static final String ANDROID_SCHEMA = "http://schemas.android.com/apk/res/android";
    private static final int SEMI_BOLD = 5;
    private static final int EXTRA_BOLD = 6;
    private static final int EXTRA_BOLD_ITALICS = 7;
    private static final int LIGHT = 8;
    private static final int MEDIUM = 9;


    public GeneralTextView(Context context) {
        this(context, null);
    }

    public GeneralTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GeneralTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyCustomFont(context, attrs, defStyle);
    }

    private void applyCustomFont(Context context, AttributeSet attrs, int defStyle) {
        TypedArray attribArray = context.obtainStyledAttributes(attrs, R.styleable.GeneralTextView);
        try {
            String fontName = attribArray.getString(R.styleable.GeneralTextView_fontName);
//        int textStyle = attrs.getAttributeIntValue(R.styleable.GeneralTextView_textStyle, 0);
            int textStyle = attribArray.getInt(R.styleable.GeneralTextView_textStyle, 0);
            if (textStyle == 0)
                textStyle = attrs.getAttributeIntValue(ANDROID_SCHEMA, "textStyle", Typeface.NORMAL);
            Typeface customFont = selectTypeface(context, fontName, textStyle);
            setTypeface(customFont);
        } finally {
            attribArray.recycle();
        }

    }

    private Typeface selectTypeface(Context context, String fontName, int textStyle) {

        switch (textStyle) {
            case MEDIUM:
                return FontCache.getTypeface(fontName + "-Medium.ttf", context);
            case Typeface.BOLD:
                return FontCache.getTypeface(fontName + "-Bold.ttf", context);
            case Typeface.ITALIC:
                return FontCache.getTypeface(fontName + "-Italic.ttf", context);
            case Typeface.BOLD_ITALIC:
                return FontCache.getTypeface(fontName + "-BoldItalic.ttf", context);
            case SEMI_BOLD:
                return FontCache.getTypeface(fontName + "-SemiBold.ttf", context);
            case EXTRA_BOLD:
                return FontCache.getTypeface(fontName + "-ExtraBold.ttf", context);
            case EXTRA_BOLD_ITALICS:
                return FontCache.getTypeface(fontName + "-ExtraBoldItalic.ttf", context);
            case LIGHT:
                return FontCache.getTypeface(fontName + "-Light.ttf", context);
            case Typeface.NORMAL:
            default:
                return FontCache.getTypeface(fontName + "-Regular.ttf", context);
        }
    }

}
