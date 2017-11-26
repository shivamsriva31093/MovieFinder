package task.application.com.colette.ui.utility.realmrecview;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import io.realm.OrderedRealmCollection;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmResults;

/**
 * Created by sHIVAM on 6/2/2017.
 */

public class RealmBaseAdapter<T extends RealmModel> extends BaseAdapter {

    @Nullable
    private OrderedRealmCollection<T> data;
    private RealmChangeListener<OrderedRealmCollection<T>> changeListener;

    public RealmBaseAdapter(@Nullable OrderedRealmCollection<T> data) {
        if (data != null && !data.isManaged())
            throw new IllegalStateException("Data is not managed by realm");
        this.data = data;
        this.changeListener = ts -> {
            notifyDataSetChanged();
        };

        if (isDatasetValid())
            addChangeListener(data);
    }

    private void addChangeListener(@NonNull OrderedRealmCollection<T> data) {
        if (data instanceof RealmResults) {
            RealmResults<T> res = (RealmResults<T>) data;
            res.addChangeListener((RealmChangeListener) changeListener);
        } else if (data instanceof RealmList) {
            RealmList<T> res = (RealmList<T>) data;
            res.addChangeListener((RealmChangeListener) changeListener);
        } else {
            throw new IllegalStateException("OrderedRealmCollection is not supported: " + data.getClass());
        }
    }

    private void removeChangeListener(@NonNull OrderedRealmCollection<T> data) {
        if (data instanceof RealmResults) {
            RealmResults<T> res = (RealmResults<T>) data;
            res.removeChangeListener((RealmChangeListener) changeListener);
        } else if (data instanceof RealmList) {
            RealmList<T> res = (RealmList<T>) data;
            res.removeChangeListener((RealmChangeListener) changeListener);
        } else {
            throw new IllegalStateException("OrderedRealmCollection is not supported: " + data.getClass());
        }
    }

    public void updateData(@Nullable OrderedRealmCollection<T> data) {
        if (changeListener != null) {
            if (isDatasetValid()) {
                removeChangeListener(this.data);
            }
            if (data != null && data.isValid()) {
                addChangeListener(data);
            }
        }

        this.data = data;
        notifyDataSetChanged();
    }


    private boolean isDatasetValid() {
        return data != null && data.isValid();
    }

    @Override
    public int getCount() {
        return isDatasetValid() ? data.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return isDatasetValid() ? data.get(i) : null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
