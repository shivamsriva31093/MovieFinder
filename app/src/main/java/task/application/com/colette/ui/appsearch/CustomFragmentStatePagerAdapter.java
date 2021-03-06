package task.application.com.colette.ui.appsearch;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

/**
 * Created by sHIVAM on 7/23/2017.
 */

public abstract class CustomFragmentStatePagerAdapter<T extends Fragment>
        extends FragmentStatePagerAdapter {

    private SparseArray<T> registeredFragments = new SparseArray<T>();

    public CustomFragmentStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        T fragment = (T) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public T getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

}
