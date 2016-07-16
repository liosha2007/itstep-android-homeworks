package edu.android.homework_07.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import icepick.Icepick;


/**
 * @author liosha on 09.06.2016.
 */
public abstract class GenericFragment<T extends Activity> extends Fragment {
    private final int fragment;
    protected SharedPreferences sharedPreferences;

    public GenericFragment(int fragment) {
        this.fragment = fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(fragment, container, false);
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        return view;
    }

    @SuppressWarnings("unchecked")
    public <T> T view(final int viewId) {
        try {
            View v = getView();
            if (v == null) {
                throw new IllegalArgumentException();
            }
            return (T) v.findViewById(viewId);
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
    public void onDestroyView() {
        sharedPreferences = null;
        super.onDestroyView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }
}
