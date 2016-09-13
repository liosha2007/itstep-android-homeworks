package edu.android.homework_15.listeners;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import edu.android.homework_15.fragment.GameFragment;

/**
 * @author liosha (13.09.2016).
 */
public class EnemyTouchListener implements View.OnTouchListener {
    private final GameFragment fragment;

    public EnemyTouchListener(GameFragment fragment) {
        this.fragment = fragment;
    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                return fragment.onEnemyDown((ImageView) view, motionEvent.getX(), motionEvent.getRawX());
            case MotionEvent.ACTION_MOVE:
                return fragment.onEnemyMove((ImageView) view, motionEvent.getX(), motionEvent.getRawX());
            case MotionEvent.ACTION_UP:
                return fragment.onEnemyUp((ImageView) view, motionEvent.getX(), motionEvent.getRawX());
            case MotionEvent.ACTION_POINTER_DOWN:
                return fragment.onEnemyDown((ImageView) view, motionEvent.getX(), motionEvent.getRawX());
            case MotionEvent.ACTION_POINTER_UP:
                return fragment.onEnemyUp((ImageView) view, motionEvent.getX(), motionEvent.getRawX());
        }
        return false;
    }

}
