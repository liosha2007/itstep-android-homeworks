package edu.android.homework_11.tasks;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import edu.android.homework_11.MainActivity;

/**
 * @author liosha (27.08.2016).
 */
public abstract class GenericTask<P, PR, R> extends AsyncTask<P, PR, R> {
    protected WeakReference<MainActivity> ref = null;

    public GenericTask(MainActivity activity) {
        this.ref = new WeakReference<>(activity);
    }
}
