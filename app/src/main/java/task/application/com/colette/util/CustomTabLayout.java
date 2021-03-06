package task.application.com.colette.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import androidx.annotation.ColorRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import task.application.com.colette.R;

public class CustomTabLayout extends TabLayout {
    private Typeface mCustomTypeFace;
    private
    @LayoutRes
    Integer customTabLayout;
    private final ArrayList<Tab> mTabs = new ArrayList<>();
    private ColorStateList mTabColors;
    private int mDefaultColor = R.color.body_text_1;

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
        mTabs.add(position, tab);
        setCustomTabView(tab, position);
    }

    @Override
    public void removeTab(Tab tab) {
        super.removeTab(tab);
        mTabs.remove(tab);
    }

    @Override
    public void removeTabAt(int position) {
        super.removeTabAt(position);
        mTabs.remove(position);
    }

    @Override
    public void removeAllTabs() {
        super.removeAllTabs();
        mTabs.clear();
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
                if (mTabColors == null)
                    tabTextView.setTextColor(ContextCompat.getColor(getContext(), mDefaultColor));
                else
                    tabTextView.setTextColor(mTabColors);
               // tabTextView.setTypeface(mCustomTypeFace, Typeface.NORMAL);
            }
        }
    }

    public void setCustomTabColors(@Nullable ColorStateList tabColors, @ColorRes int defaultColor) {
        mTabColors = tabColors;
        mDefaultColor = defaultColor;
    }
}