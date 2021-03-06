package edu.android.homework_07.fragment;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

import edu.android.homework_07.R;
import edu.android.homework_07.activity.MainActivity;
import edu.android.homework_07.data.Answer;
import edu.android.homework_07.data.Question;
import edu.android.homework_07.saver.RecordsSaver;

/**
 * @author liosha on 09.06.2016.
 */
public class RecordsFragment extends GenericFragment<MainActivity> implements RecordsSaver.OnRecordsLoadListener {
    private TextView txv_records;
    public RecordsFragment() {
        super(R.layout.fragment_records);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txv_records = view(R.id.txv_records);

        loadAndShowRecords();
    }

    private void loadAndShowRecords() {
        final RecordsSaver recordsSaver = getCurrentActivity().getRecordsSaver();
        if (recordsSaver != null && recordsSaver.canLoad(getActivity())) {
            recordsSaver.setOnRecordsLoadListener(this);
            recordsSaver.loadRecords(getActivity());
        }
    }

    @Override
    public void onGetRecords(StringBuilder records) {
        txv_records.setText(Html.fromHtml(records.toString()));
    }

    @Override
    public void onRecordsLoaded() {

    }
}
