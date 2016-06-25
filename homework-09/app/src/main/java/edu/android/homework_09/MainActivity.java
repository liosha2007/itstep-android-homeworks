package edu.android.homework_09;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity implements View.OnClickListener, Animator.AnimatorListener {
    private static final Random RANDOM = new Random(Calendar.getInstance().getTimeInMillis());
    private Map<Animator, View> enemies = new HashMap<>();
    private int score = 0;
    private int live = 10;
    private View imv_player;
    private TextView txv_score;
    private TextView txv_live;
    private final int enemySize = 100;
    private RelativeLayout rll_root;
    private final Handler uiHandler = new Handler();
    private Timer myTimer;
    private final Runnable runnableStart = new Runnable() {
        @Override
        public void run() {
            createEnemy();
            startTimer();
        }
    };
    private int width;
    private int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imv_player = view(R.id.imv_player);
        txv_score = view(R.id.txv_score);
        rll_root = view(R.id.rll_root);
        txv_live = view(R.id.txv_live);

        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point outSize = new Point();
        display.getSize(outSize);
        this.width = outSize.x;
        this.height = outSize.y;

        startGame();
    }

    private void startGame() {
        txv_score.setText(getString(R.string.score, score));
        txv_live.setText(getString(R.string.live, live));

        myTimer = new Timer();
        startTimer();
    }

    private void startTimer() {
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                uiHandler.post(runnableStart);
            }
        }, 3L * RANDOM.nextInt(1000)); // интервал - 3000 миллисекунд, 0 миллисекунд до первого запуска.

    }

    private void createEnemy() {

        final int left = RANDOM.nextInt(width - enemySize);

        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(enemySize, enemySize);

        final View enemy = new View(this);
        enemy.setX(left);
        enemy.setLayoutParams(layoutParams);
        enemy.setBackgroundColor(Color.YELLOW);
        enemy.setOnClickListener(this);
        rll_root.addView(enemy);

        createEnemyAnimation(enemy).start();

    }

    private AnimatorSet createEnemyAnimation(View enemy) {
        final int bottom = height - enemySize - 200;
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.addListener(this);

        ObjectAnimator rotation = ObjectAnimator.ofFloat(enemy, "rotation", 0, 360);
        rotation.setDuration(1000);
        rotation.setRepeatCount(2);
        rotation.setInterpolator(new LinearInterpolator());

        ObjectAnimator y = ObjectAnimator.ofFloat(enemy, "y", 0, bottom);
        y.setDuration(3000);
        y.setInterpolator(new LinearInterpolator());

        animatorSet.playTogether(rotation, y);
        enemies.put(animatorSet, enemy);
        return animatorSet;
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

    @Override
    public void onClick(View v) {
        enemies.values().remove(v);
        rll_root.removeView(v);
        txv_score.setText(getString(R.string.score, score += 100));
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        final View enemy = enemies.remove(animation);
        if (enemy != null) {
            enemy.setOnClickListener(null);
            Log.d("MY", "onAnimationEnd...");
            txv_live.setText(getString(R.string.live, live -= 1));
            if (live <= 0) {
                rll_root.removeView(enemy);
                processEnemyWin(enemy.getX());
            } else {
                enemy.startAnimation(createExplosionAnimation(enemy));
            }
        }
    }

    private Animation createExplosionAnimation(final View enemy) {
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.explosion);
        enemy.setBackgroundColor(Color.RED);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        rll_root.removeView(enemy);
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        return animation;
    }

    private void processEnemyWin(float x) {
        Log.d("MY", "Game Over!");
        myTimer.cancel();
        for (Animator animator : enemies.keySet()) {
            rll_root.removeView(enemies.get(animator));
        }
        enemies.clear();
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setPositiveButton("Заново", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        score = 0;
                        live = 10;
                        startGame();
                    }
                })
                .setNegativeButton("Выйти", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setMessage("Игра окончена!\nВаши очки: " + score)
                .create().show();
    }

    @Override
    public void onAnimationStart(Animator animation) {}

    @Override
    public void onAnimationCancel(Animator animation) { }

    @Override
    public void onAnimationRepeat(Animator animation) { }
}
