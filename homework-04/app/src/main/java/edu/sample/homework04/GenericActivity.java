package edu.sample.homework04;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;

import org.xmlpull.v1.XmlPullParser;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liosha on 16.04.2016.
 */
public class GenericActivity extends AppCompatActivity {

    @SuppressWarnings("unchecked")
    public <T> T view(int viewId) {
        try {
            return (T) findViewById(viewId);
        } catch (ClassCastException e) {
            Log.e("MY", "Can't cast view (" + viewId + ") to target tipe! " + e.getMessage());
        }
        return null;
    }

    public String string(int stringId) {
        return getResources().getString(stringId);
    }

    /**
     * Sets ListView height dynamically based on the height of the items.
     *
     * @param listView to be resized
     * @return true if the listView is successfully resized, false otherwise
     */
    public static boolean setListViewHeightBasedOnItems(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return false;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getLayoutParams().height;
//            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
        return true;
    }
    public static Map<String, String> getHashMapResource(Context context, int hashMapResId) {
        Map<String, String> map = new HashMap<>();
        XmlResourceParser parser = context.getResources().getXml(hashMapResId);

        String key = null, value = null;

        try {
            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (parser.getName().equals("entry")) {
                        key = parser.getAttributeValue(null, "key");

                        if (null == key) {
                            parser.close();
                            return null;
                        }
                    }
                }
                else if (eventType == XmlPullParser.END_TAG) {
                    if (parser.getName().equals("entry")) {
                        map.put(key, value);
                        key = null;
                        value = null;
                    }
                } else if (eventType == XmlPullParser.TEXT) {
                    if (null != key) {
                        value = parser.getText();
                    }
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return map;
    }


    protected String formQueryCheckBox(String name, CheckBox checkBox, String value) {
        if (checkBox.isChecked()) {
            return name + "=" + value;
        }
        return null;
    }

    protected String formQueryForPrice(String name, EditText from, EditText to) {
        final String fromPrice = from.getText().toString();
        final String toPrice = to.getText().toString();
        if (!fromPrice.isEmpty() && !toPrice.isEmpty()) {
            return name + "=" + fromPrice + "-" + toPrice;
        }
        return null;
    }

    protected String formQueryForSensor(String name, RadioButton sensorYes, RadioButton sensorNo) {
        if (sensorYes.isChecked()) {
            return name + "=23458";
        } else if (sensorNo.isChecked()) {
            return name + "=23459";
        }
        return null;
    }

    protected String formQuerySpinner(String key, Spinner spinner) {
        final View view = spinner.getSelectedView();
        if (view != null) {
            final Object tag = view.getTag();
            if (tag != null) {
                return key + "=" + (String) tag;
            }
        }
        return null;
    }

}
