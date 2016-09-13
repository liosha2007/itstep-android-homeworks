package edu.android.homework_15.fragment;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import edu.android.homework_15.listeners.EnemyTouchListener;
import edu.android.homework_15.MainActivity;
import edu.android.homework_15.listeners.PlayerTouchListener;
import edu.android.homework_15.R;
import edu.android.homework_15.listeners.RootTouchListener;

public class GameFragment extends GenericFragment<MainActivity> implements Runnable {
    private static final float NONE = -1f;

    private ImageView imv_player;
    private ImageView imv_enemy;
    private ImageView imv_ball;

    private LinearLayout lnl_enemyLives;
    private LinearLayout lnl_playerLives;

    private TextView txv_enemy_win;
    private TextView txv_player_win;

    private float playerOffsetX = NONE;
    private float enemyOffsetX = NONE;
    private Timer timer;
    private int screenX;
    private int screenY;
    private int ballX;
    private int ballY;
    private int ballSpeed = 3;
    private float vectorX = 1;
    private float vectorY = 1;
    private View vew_root;

    public GameFragment() {
        super(R.layout.fragment_game);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        (imv_player = view(R.id.imv_player)).setOnTouchListener(new PlayerTouchListener(this));
        (imv_enemy = view(R.id.imv_enemy)).setOnTouchListener(new EnemyTouchListener(this));
        imv_ball = view(R.id.imv_ball);
        (vew_root = ((View) imv_enemy.getParent())).setOnTouchListener(new RootTouchListener(this));

        lnl_enemyLives = view(R.id.lnl_enemyLives);
        lnl_playerLives = view(R.id.lnl_playerLives);

        txv_enemy_win = view(R.id.txv_enemy_win);
        txv_player_win = view(R.id.txv_player_win);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenX = metrics.widthPixels - 50;
        screenY = metrics.heightPixels - 10;

        ballX = screenX / 2 - imv_ball.getWidth() / 2;
        ballY = screenY / 2 - imv_ball.getHeight() / 2;

        this.timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                GameFragment.this.getCurrentActivity().runOnUiThread(GameFragment.this);
            }
        }, 0, 5);
    }

    public boolean onRootDown(float x, float y) {
        return true;
    }

    public boolean onRootMove(float x, float y) {
        return false;
    }

    public boolean onRootUp(float x, float y) {
        return false;
    }

    public boolean onPlayerDown(ImageView view, float x, float xRaw) {
        this.playerOffsetX = x;
        return true;
    }

    public boolean onPlayerMove(ImageView view, float x, float xRaw) {
        final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.leftMargin = (int) (xRaw - playerOffsetX);
        view.setLayoutParams(params);
        return true;
    }

    public boolean onPlayerUp(ImageView view, float x, float xRaw) {
        return false;
    }

    public boolean onEnemyDown(ImageView view, float x, float xRaw) {
        this.enemyOffsetX = x;
        return true;
    }

    public boolean onEnemyMove(ImageView view, float x, float xRaw) {
        final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.leftMargin = (int) (xRaw - enemyOffsetX);
        view.setLayoutParams(params);
        return true;
    }

    public boolean onEnemyUp(ImageView view, float x, float xRaw) {
        return false;
    }

    float playerMoveX = 0;
    float playerMoveY = 0;

    void playerMove() {
        ballX += (int) (playerMoveX * ballSpeed);
        ballY += (int) (playerMoveY * ballSpeed);
    }

    void PlayerVector(float _x, float _y) {
        playerMoveX = _x;
        playerMoveY = _y;

        // Теперь нужно превратить наш вектор в единичную длину, сначала найдём длину вектора
        // Это корень квадратный из суммы компонентов вектора, которые в свою очередь были возведены в квадрат
        float len = (float) Math.sqrt(playerMoveX * playerMoveX + playerMoveY * playerMoveY);

        // Теперь надо каждый компонент разделить на длину (это называется нормализация вектора)
        playerMoveX /= len;
        playerMoveY /= len;
    }

    @Override
    public void run() {
        updateVector();
        playerMove();

        final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imv_ball.getLayoutParams();
        params.leftMargin = ballX;
        params.topMargin = ballY;
        imv_ball.setLayoutParams(params);
    }

    private void updateVector() {

        checkArea();
        checkPlayer();
        checkEnemy();
        checkPlayerLives();
        checkEnemyLives();

        PlayerVector(vectorX, vectorY);
    }

    private void checkPlayerLives() {
        final int childCount = lnl_playerLives.getChildCount();
        for (int n = 0; n < childCount; n++) {
            final View view = lnl_playerLives.getChildAt(n);
            if (view != null) {
                final float x = view.getX() + lnl_playerLives.getX();
                final float y = view.getY() + lnl_playerLives.getY();
                final int width = view.getWidth();
                if (ballY + imv_ball.getHeight() >= y && ballX > x && ballX < x + width) {
                    lnl_playerLives.removeView(view);
                    vectorY = -1;
                    break;
                }
            }
        }
        if (lnl_playerLives.getChildCount() == 0) {
            txv_enemy_win.setVisibility(View.VISIBLE);
            vectorX = vectorY = 0;
            timer.cancel();
        }
    }

    private void checkEnemyLives() {
        final int childCount = lnl_enemyLives.getChildCount();
        for (int n = 0; n < childCount; n++) {
            final View view = lnl_enemyLives.getChildAt(n);
            if (view != null) {
                final float x = view.getX() + lnl_enemyLives.getX();
                final float y = view.getY() + lnl_enemyLives.getY() + view.getHeight();
                final int width = view.getWidth();
                if (ballY <= y && ballX > x && ballX < x + width) {
                    lnl_enemyLives.removeView(view);
                    vectorY = 1;
                    break;
                }
            }
        }
        if (lnl_enemyLives.getChildCount() == 0) {
            txv_player_win.setVisibility(View.VISIBLE);
            vectorX = vectorY = 0;
            timer.cancel();
        }
    }

    private void checkEnemy() {
        final float x = imv_enemy.getX();
        final float y = imv_enemy.getY() + imv_enemy.getHeight();
        final int width = imv_enemy.getWidth();
        if (ballY <= y && ballX > x && ballX < x + width) {
            vectorY = 1;
        }
    }

    private void checkPlayer() {
        final float x = imv_player.getX();
        final float y = imv_player.getY() - imv_ball.getHeight();
        final int width = imv_player.getWidth();
        if (ballY >= y && ballX > x && ballX < x + width) {
            vectorY = -1;
        }
    }

    private void checkArea() {
        if (ballX <= 0) {
            vectorX = 1;
        }
        if (ballX >= screenX) {
            vectorX = -1;
        }
        if (ballY <= 0) {
            vectorY = 1;
        }
        if (ballY >= screenY) {
            vectorY = -1;
        }
    }

    @Override
    public void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }
}
