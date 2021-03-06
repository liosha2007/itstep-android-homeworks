package edu.android.homework_15.listeners;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import edu.android.homework_15.fragment.GameFragment;

/**
 * @author liosha (13.09.2016).
 */
public class PlayerTouchListener implements View.OnTouchListener {
    private final GameFragment fragment;

    public PlayerTouchListener(GameFragment fragment) {
        this.fragment = fragment;
    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                return fragment.onPlayerDown((ImageView) view, motionEvent.getX(), motionEvent.getRawX());
            case MotionEvent.ACTION_MOVE:
                return fragment.onPlayerMove((ImageView) view, motionEvent.getX(), motionEvent.getRawX());
            case MotionEvent.ACTION_UP:
                return fragment.onPlayerUp((ImageView) view, motionEvent.getX(), motionEvent.getRawX());
            case MotionEvent.ACTION_POINTER_DOWN:
                return fragment.onPlayerDown((ImageView) view, motionEvent.getX(), motionEvent.getRawX());
            case MotionEvent.ACTION_POINTER_UP:
                return fragment.onPlayerUp((ImageView) view, motionEvent.getX(), motionEvent.getRawX());
        }
        return false;
    }

}
