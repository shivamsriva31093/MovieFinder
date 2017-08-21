package task.application.com.moviefinder.util;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CustomTabLayout extends TabLayout {
    private Typeface mCustomTypeFace;

    public CustomTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialise();
    }

    public CustomTabLayout(Context context) {
        super(context);
        initialise();
    }

    public CustomTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialise();
    }

    private void initialise() {
        // Note: Rename "EdgeCaps.tff" to whatever your font file is named.
        // Note that the font file needs to be in the assets/fonts/ folder.
        mCustomTypeFace = FontCache.getTypeface("Nunito-SemiBold.ttf", getContext());
    }

    @Override
    public void addTab(@NonNull Tab tab, boolean setSelected) {
        super.addTab(tab, setSelected);
        setTabTypeFace(tab);
    }

    @Override
    public void addTab(@NonNull Tab tab, int position, boolean setSelected) {
        super.addTab(tab, position, setSelected);
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
                tabTextView.setTypeface(mCustomTypeFace, Typeface.NORMAL);
            }
        }
    }
}