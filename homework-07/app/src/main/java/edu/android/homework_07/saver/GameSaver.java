package edu.android.homework_07.saver;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import edu.android.homework_07.data.GameState;

/**
 * @author liosha (02.07.2016).
 */
public class GameSaver {
    private final String STATE_FILE_PATH;

    private OnGameSaveListener gameSaveListener = null;
    private OnGameLoadListener gameLoadListener = null;

    public GameSaver(Context context) {
        STATE_FILE_PATH = context.getFilesDir().getAbsolutePath() + File.separator + "state.ser";
    }

    public boolean canSave(Context context) {
        final File file = new File(STATE_FILE_PATH);
        return !file.exists() || file.canWrite();
    }
    public boolean canLoad(Context context) {
        final File file = new File(STATE_FILE_PATH);
        return file.exists() && file.canRead();
    }
    public GameSaver setOnGameSaveListener(OnGameSaveListener gameSaveListener) {
        this.gameSaveListener = gameSaveListener;
        return this;
    }
    public GameSaver setOnGameLoadListener(OnGameLoadListener gameLoadListener) {
        this.gameLoadListener = gameLoadListener;
        return this;
    }

    public boolean saveGame(Context context) {
        final GameState gameState = new GameState();
        try {
            FileOutputStream fos = new FileOutputStream(STATE_FILE_PATH);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            if (gameSaveListener != null) {
                gameSaveListener.onPutGameState(gameState);
            }
            oos.writeObject(gameState);
            oos.flush();
            oos.close();
        } catch (Exception e) {
            Log.e("MY", "Can't save game!", e);
            return false;
        }
        if (gameSaveListener != null) {
            gameSaveListener.onGameSaved();
        }
        return true;
    }

    public boolean loadGame(Context context) {
        final GameState gameState;
        try {
            FileInputStream fos = new FileInputStream(STATE_FILE_PATH);
            ObjectInputStream oos = new ObjectInputStream(fos);
            gameState = (GameState) oos.readObject();
            oos.close();
        } catch (Exception e) {
            Log.e("MY", "Can't load game!", e);
            return false;
        }
        if (gameLoadListener != null) {
            gameLoadListener.onGetGameState(gameState);
            gameLoadListener.onGameLoaded();
        }
        return true;
    }

    public interface OnGameSaveListener {
        void onPutGameState(GameState gameState);
        void onGameSaved();
    }
    public interface OnGameLoadListener {
        void onGetGameState(GameState gameState);
        void onGameLoaded();
    }
}
