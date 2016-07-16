package edu.android.homework_07.saver;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Scanner;

import edu.android.homework_07.Utils;

/**
 * @author liosha (02.07.2016).
 */
public class RecordsSaver {
    private final String RECORDS_FILE_PATH;

    private OnRecordsSaveListener recordsSaveListener = null;
    private OnRecordsLoadListener recordsLoadListener = null;

    public RecordsSaver(Context context) {
        RECORDS_FILE_PATH = context.getFilesDir().getAbsolutePath() + File.separator + "records.ser";
    }

    public boolean canSave(Context context) {
        final File file = new File(RECORDS_FILE_PATH);
        return !file.exists() || file.canWrite();
    }

    public boolean canLoad(Context context) {
        final File file = new File(RECORDS_FILE_PATH);
        return file.exists() && file.canRead();
    }
    public RecordsSaver setOnRecordsSaveListener(OnRecordsSaveListener recordsSaveListener) {
        this.recordsSaveListener = recordsSaveListener;
        return this;
    }
    public RecordsSaver setOnRecordsLoadListener(OnRecordsLoadListener recordsLoadListener) {
        this.recordsLoadListener = recordsLoadListener;
        return this;
    }

    public boolean saveRecords(Context context, boolean isKeepSum) {
        try {
            FileOutputStream internalFileOutputStream = new FileOutputStream(RECORDS_FILE_PATH, true);
            PrintWriter internalWriter = new PrintWriter(internalFileOutputStream);
            StringBuilder records = new StringBuilder();
            if (recordsSaveListener != null) {
                recordsSaveListener.onPutRecords(records, isKeepSum);
            }
            internalWriter.write(records.toString());
            internalWriter.close();
            internalFileOutputStream.close();
        } catch (Exception e) {
            Log.d("MY", "Can't save records! ", e);
            return false;
        }
        if (recordsSaveListener != null) {
            recordsSaveListener.onRecordsSaved();
        }
        return true;
    }

    public boolean loadRecords(Context context) {
        try {
            File file = new File(RECORDS_FILE_PATH);
            Scanner scanner = new Scanner(file);
            StringBuilder builder = new StringBuilder();
            while (scanner.hasNext()) {
                builder.append(scanner.next());
            }
            scanner.close();
            if (recordsLoadListener != null) {
                recordsLoadListener.onGetRecords(builder);
                recordsLoadListener.onRecordsLoaded();
            }

        } catch (Exception e) {
            Log.d("MY", "Can't load records! ", e);
            return false;
        }

        return true;
    }

    public interface OnRecordsSaveListener {
        void onPutRecords(StringBuilder records, boolean isKeepSum);
        void onRecordsSaved();
    }
    public interface OnRecordsLoadListener {
        void onGetRecords(StringBuilder records);
        void onRecordsLoaded();
    }
}
