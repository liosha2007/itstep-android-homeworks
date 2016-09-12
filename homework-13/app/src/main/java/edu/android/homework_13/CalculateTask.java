package edu.android.homework_13;

import android.os.AsyncTask;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.Scanner;

/**
 * @author liosha (13.09.2016).
 */
public class CalculateTask extends AsyncTask<String, Void, String> {
    private final WeakReference<MainActivity> ref;

    public CalculateTask(MainActivity activity) {
        this.ref = new WeakReference<>(activity);
    }

    @Override
    protected String doInBackground(String... strings) {
        String sentence = strings[0].replace('x', 'X').replace("+", " + ").replace("-", " - ").replace("*", " * ").replace("/", " / ").replace("=", " = ");
            Scanner sc = new Scanner(sentence);
            String
                    left = sc.next("[X\\s\\d\\.]+").trim(),
                    operator = sc.next("[\\+\\-\\*/\\s]+").trim(),
                    right = sc.next("[X\\s\\d\\.]+").trim(),
                    result = sc.skip("[\\s=]+").next("[X\\s\\d\\.]+").trim();

            if ("X".equalsIgnoreCase(left)) {
                sentence += ",   X = " + calculate(result, opositeTo(operator), right);
            } else if ("X".equalsIgnoreCase(right)) {
                sentence += ",   X = " + calculate(result, opositeTo(operator), left);
            } else {
                sentence += ",   X = " + calculate(left, operator, right);
            }

        return sentence;
    }

    private String opositeTo(String operator) {
        return "*".equals(operator) ? "/" : "+".equals(operator) ? "-" : "+";
    }

    private String calculate(String left, String operator, String right) {
        if ("+".equals(operator)) {
            return Double.toString(Double.parseDouble(left) + Double.parseDouble(right));
        }
        if ("-".equals(operator)) {
            return Double.toString(Double.parseDouble(left) - Double.parseDouble(right));
        }
        if ("*".equals(operator)) {
            return Double.toString(Double.parseDouble(left) * Double.parseDouble(right));
        }
        return Double.toString(Double.parseDouble(left) / Double.parseDouble(right));
    }

    @Override
    protected void onPostExecute(String sentence) {
        ref.get().onSentenceCalculated(sentence);
    }
}
