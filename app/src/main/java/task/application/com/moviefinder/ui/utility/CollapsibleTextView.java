package task.application.com.moviefinder.ui.utility;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;

import task.application.com.moviefinder.R;

/**
 * Created by sHIVAM on 8/26/2017.
 */

public class CollapsibleTextView extends GeneralTextView {
    private static final int TRIM_LENGTH_DEF = 200;
    private static final String ELLIPSIS = "....";

    private CharSequence originalText;
    private CharSequence trimmedText;
    private BufferType bufferType;
    private boolean trim = true;
    private int trimLength;

    public CollapsibleTextView(Context context) {
        this(context, null);
    }

    public CollapsibleTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CollapsibleTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CollapsibleTextView);
        trimLength = typedArray.getInt(R.styleable.CollapsibleTextView_trimLength, TRIM_LENGTH_DEF);
        typedArray.recycle();

        setOnClickListener(view -> {
            trim = !trim;
            setText();
            requestFocusFromTouch();
        });
    }

    private void setText() {
        super.setText(trim ? trimmedText : originalText, bufferType);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        originalText = text;
        trimmedText = getTrimmedText(text);
        bufferType = type;
        setText();
    }

    public CharSequence getOriginalText() {
        return originalText;
    }

    public void setTrimLength(int trimLength) {
        this.trimLength = trimLength;
        trimmedText = getTrimmedText(originalText);
        setText();
    }

    public int getTrimLength() {
        return trimLength;
    }

    private CharSequence getTrimmedText(CharSequence text) {
        if (originalText != null && originalText.length() > trimLength) {
            return new SpannableStringBuilder(originalText, 0, trimLength + 1).append(ELLIPSIS);
        } else {
            return originalText;
        }
    }
}
