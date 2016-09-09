package edu.android.arterialbloodpressure.tasks;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import edu.android.arterialbloodpressure.MainActivity;
import edu.android.arterialbloodpressure.fragment.MainFragment;


/**
 * @author liosha (27.08.2016).
 */
public abstract class GenericTask<C, P, PR, R> extends AsyncTask<P, PR, R> {
    protected WeakReference<C> ref = null;

    public GenericTask(C controller) {
        this.ref = new WeakReference<>(controller);
    }
}
