package edu.android.homework_15.listeners;

import android.view.MotionEvent;
import android.view.View;

import edu.android.homework_15.fragment.GameFragment;

/**
 * @author liosha (13.09.2016).
 */
public class RootTouchListener implements View.OnTouchListener {
    private final GameFragment fragment;

    public RootTouchListener(GameFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                return fragment.onRootDown(motionEvent.getX(), motionEvent.getY());
            case MotionEvent.ACTION_MOVE:
                return fragment.onRootMove(motionEvent.getX(), motionEvent.getY());
            case MotionEvent.ACTION_UP:
                return fragment.onRootUp(motionEvent.getX(), motionEvent.getY());
            case MotionEvent.ACTION_POINTER_DOWN:
                return fragment.onRootDown(motionEvent.getX(), motionEvent.getY());
            case MotionEvent.ACTION_POINTER_UP:
                return fragment.onRootUp(motionEvent.getX(), motionEvent.getY());
        }
        return false;
    }

}
