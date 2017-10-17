package task.application.com.moviefinder.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import task.application.com.moviefinder.R;

public class CustomTabLayout extends TabLayout {
    private Typeface mCustomTypeFace;
    private
    @LayoutRes
    Integer customTabLayout;

    public CustomTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialise(context, attrs, defStyleAttr);
    }

    public CustomTabLayout(Context context) {
        this(context, null);
    }

    public CustomTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private void initialise(Context context, AttributeSet attrs, int defStyleAttr) {
        // Note: Rename "EdgeCaps.tff" to whatever your font file is named.
        // Note that the font file needs to be in the assets/fonts/ folder.
        TypedArray attrib = context.obtainStyledAttributes(attrs, R.styleable.CustomTabLayout);
        try {
            String fontName = attrib.getString(R.styleable.CustomTabLayout_tabFont);
            customTabLayout = attrib.getResourceId(R.styleable.CustomTabLayout_tabLayout, R.layout.custom_tabview);
            mCustomTypeFace = FontCache.getTypeface(fontName + ".ttf", getContext());
        } finally {
            attrib.recycle();
        }
    }

    @Override
    public void addTab(@NonNull Tab tab, int position, boolean setSelected) {
        super.addTab(tab, position, setSelected);
        setCustomTabView(tab, position);
    }

    @Override
    public void addOnTabSelectedListener(@NonNull OnTabSelectedListener listener) {
        super.addOnTabSelectedListener(listener);
    }

    private void setCustomTabView(@NonNull Tab tab, int position) {
        tab.setCustomView(customTabLayout);
        setTabTypeFace(tab);
    }

    private void setTabTypeFace(Tab tab) {
        ViewGroup tabLayoutView = (ViewGroup) getChildAt(0);
        ViewGroup tabView = (ViewGroup) tabLayoutView.getChildAt(tab.getPosition());
        int tabViewChildCount = tabView.getChildCount();
        for (int i = 0; i < tabViewChildCount; i++) {
            View tabViewChild = tabView.getChildAt(i);
            // Find the TextView in the tab
            if (tabViewChild instanceof TextView) {
                TextView tabTextView = (TextView) tabViewChild;
                // Set the TextView's font
                tabTextView.setTextColor(getResources().getColor(R.color.white));
                tabTextView.setTypeface(mCustomTypeFace, Typeface.NORMAL);
            }
        }
    }
}