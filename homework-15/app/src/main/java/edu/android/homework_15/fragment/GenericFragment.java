package edu.android.homework_15.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * @author liosha on 09.06.2016.
 */
public abstract class GenericFragment<T extends Activity> extends android.support.v4.app.Fragment {
    private final int fragment;

    public GenericFragment(int fragment) {
        this.fragment = fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(fragment, container, false);
    }

    @SuppressWarnings("unchecked")
    public <V extends View> V view(final int viewId) {
        try {
            View v = getView();
            if (v == null) {
                throw new IllegalArgumentException();
            }
            return (V) v.findViewById(viewId);
        } catch (ClassCastException e) {
            Log.e("MY", "Can't cast view from fragment to target type! View ID: " + viewId);
        } catch (IllegalArgumentException e) {
            Log.e("MY", "Can't get view of fragment! Fragment: " + fragment);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    protected T getCurrentActivity() {
        return (T) getActivity();
    }

    @Override
    public abstract void onViewCreated(View view, Bundle savedInstanceState);
}
