package task.application.com.moviefinder.util;

import android.text.ParcelableSpan;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sHIVAM on 7/10/2017.
 */

public class CustomSpannableStringBuilder {

    private class SpanSection {
        private final String text;
        private final int startIdx;
        private final ParcelableSpan[] spans;

        public SpanSection(String text, int startIdx, ParcelableSpan... spans) {
            this.spans = spans;
            this.startIdx = startIdx;
            this.text = text;
        }

        public void apply(SpannableStringBuilder spannableStringBuilder) {
            if (spannableStringBuilder == null)
                return;
            for (ParcelableSpan parcelableSpan : spans) {
                spannableStringBuilder.setSpan(parcelableSpan, startIdx, startIdx + text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    private List<SpanSection> spanSections;
    private StringBuilder stringBuilder;

    public CustomSpannableStringBuilder() {
        spanSections = new ArrayList<>();
        stringBuilder = new StringBuilder();
    }

    public CustomSpannableStringBuilder append(String text, ParcelableSpan... spans) {
        if (spans != null && spans.length > 0)
            spanSections.add(new SpanSection(text, stringBuilder.length(), spans));
        stringBuilder.append(text);
        return this;
    }

    public SpannableStringBuilder build() {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(stringBuilder.toString());
        for (SpanSection section : spanSections) {
            section.apply(spannableStringBuilder);
        }
        return spannableStringBuilder;
    }

    @Override
    public String toString() {
        return stringBuilder.toString();
    }
}
