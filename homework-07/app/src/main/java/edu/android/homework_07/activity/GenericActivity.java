package edu.android.homework_07.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.xmlpull.v1.XmlPullParser;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liosha on 03.06.2016.
 */
public abstract class GenericActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState, int activity) {
        super.onCreate(savedInstanceState);
        setContentView(activity);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @SuppressWarnings("unchecked")
    public <T> T view(final int viewId) {
        try {
            return (T) findViewById(viewId);
        } catch (ClassCastException e) {
            Log.e("MY", "Can't cast view to target type! View ID: " + viewId);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> T view(final Fragment fragment, final int viewId) {
        try {
            View v = fragment.getView();
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
    public <T> T view(final View v, final int viewId) {
        try {
            if (v == null) {
                throw new IllegalArgumentException();
            }
            return (T) v.findViewById(viewId);
        } catch (ClassCastException e) {
            Log.e("MY", "Can't cast view to target type! View ID: " + viewId);
        } catch (IllegalArgumentException e) {
            Log.e("MY", "Can't get view! View: " + v);
        }
        return null;
    }

    public <T extends Fragment> T fragment(String tag) {
        final Fragment variants = getFragmentManager().findFragmentByTag(tag);
        if (variants != null) {
            return (T) variants;
        }
        return null;
    }
}
