package edu.android.homework_07.activity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.ArrayUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import edu.android.homework_07.R;
import edu.android.homework_07.Utils;
import edu.android.homework_07.saver.GameSaver;
import edu.android.homework_07.data.GameState;
import edu.android.homework_07.data.Question;
import edu.android.homework_07.fragment.AboutFragment;
import edu.android.homework_07.fragment.InfoFragment;
import edu.android.homework_07.fragment.MenuFragment;
import edu.android.homework_07.fragment.QuestionFragment;
import edu.android.homework_07.fragment.RecordsFragment;
import edu.android.homework_07.fragment.VariantsFragment;
import edu.android.homework_07.saver.RecordsSaver;
import icepick.State;

public class MainActivity extends GenericActivity implements GameSaver.OnGameSaveListener, GameSaver.OnGameLoadListener, RecordsSaver.OnRecordsSaveListener {
    public static final Random RANDOM = new Random(new Date().getTime());
    public static final String MENU_TAG = "MENU_TAG";
    public static final String INFO_TAG = "INFO_TAG";
    public static final String QUESTION_TAG = "QUESTION_TAG";
    public static final String VARIANTS_TAG = "VARIANTS_TAG";
    public static final String RECORDS_TAG = "RECORDS_TAG";
    public static final String ABOUT_TAG = "ABOUT_TAG";
    private GameSaver gameSaver;
    private RecordsSaver recordsSaver;
    //    private FrameLayout frl_top;
//    private FrameLayout frl_center;
//    private FrameLayout frl_bottom;
    @State
    public int goldIndex = 0;
    @State
    public String userName;
    @State
    public boolean is50x50Used = false;
    @State
    public boolean isCallUsed = false;
    @State
    public boolean isZalUsed = false;
    private List<Integer> usedQuestions;
    private List<Question> questions = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameSaver = new GameSaver(this).setOnGameSaveListener(this).setOnGameLoadListener(this);
        recordsSaver = new RecordsSaver(this).setOnRecordsSaveListener(this);

        // TODO: savedInstanceState use it!

        this.questions = Utils.loadQuestions(this, R.xml.questions);
        showMenu();
    }

    private void showMenu() {
        removeFragmentsByTag(getFragmentManager(), INFO_TAG, QUESTION_TAG)
                .replace(R.id.frl_bottom, new MenuFragment(), MENU_TAG)
                .commit();
    }

    public void loadGame() {
        if (gameSaver.canLoad(this)) {
            gameSaver.loadGame(this);
        } else {
            Toast.makeText(this, "Не удалось загрузить игру!", Toast.LENGTH_SHORT).show();
        }
    }

    public void startNewGame() {
        usedQuestions = new ArrayList<>();
        resetState();
        startGame();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View view = getLayoutInflater().inflate(R.layout.dialog_username, null);
        AlertDialog alertDialog = builder
                .setCancelable(false) // Не закрывать по клику вне диалога
                .setView(view)
                .setNeutralButton("Ок", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.userName = MainActivity.this.<TextView>view(view, R.id.edt_username).getText().toString();
                        if (MainActivity.this.userName.isEmpty()) {
                            MainActivity.this.userName = "Noname";
                        }
                        showNextQuestion();
                        dialog.cancel();
                    }
                })
                .create();
        alertDialog.show();

    }

    private void startGame() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.frl_top, new InfoFragment(), INFO_TAG)
                .replace(R.id.frl_bottom, new VariantsFragment(), VARIANTS_TAG)
                .replace(R.id.frl_center, new QuestionFragment(), QUESTION_TAG)
                .addToBackStack(null).commit();
    }

    public void showRecords() {
        final FragmentManager fragmentManager = getFragmentManager();
        removeFragmentsByTag(fragmentManager, INFO_TAG, MENU_TAG)
                .replace(R.id.frl_center, new RecordsFragment(), RECORDS_TAG)
                .addToBackStack(null).commit();

    }

    public void showAbout() {
        final FragmentManager fragmentManager = getFragmentManager();
        removeFragmentsByTag(fragmentManager, INFO_TAG, MENU_TAG)
                .replace(R.id.frl_center, new AboutFragment(), ABOUT_TAG)
                .addToBackStack(null).commit();
    }

    private FragmentTransaction removeFragmentsByTag(FragmentManager fragmentManager, String... tags) {
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        for (String tag : tags) {
            final Fragment menu = fragmentManager.findFragmentByTag(tag);
            if (menu != null) {
                fragmentTransaction.remove(menu);
            }
        }
        return fragmentTransaction;
    }

    public void exit() {
        finish();
    }

    private void resetState() {
        goldIndex = -1;
        is50x50Used =  false;
        isCallUsed = false;
        isZalUsed = false;
    }

    public void showVariants(Question question) {
        final VariantsFragment fragment;
        if ((fragment = fragment(VARIANTS_TAG)) != null) {
            fragment.showVariants(question);
        }
    }

    public void processCorrectAnswer() {
        Log.d("MY", "Process correct answer...");
        View dialogView;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (!Utils.isFinalPrize(goldIndex)) {
            goldIndex++;
            updateCurrentGold();
            dialogView = getLayoutInflater().inflate(R.layout.dialog_correct, null);
            this.<TextView>view(dialogView, R.id.txv_got_gold).setText(getString(R.string.you_got_gold, Utils.getGold(goldIndex)));
            builder.setNegativeButton("Сохранить и выйти", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    saveRecord(true);
                    saveGame();
                    resetState();
                    showMenu();
                }
            });
            builder.setPositiveButton(R.string.next_question, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (!Utils.isFinalPrize(goldIndex)) {
                        showNextQuestion();
                    }
                    dialog.cancel();
                }
            });
        } else {
            saveRecord(true);
            dialogView = getLayoutInflater().inflate(R.layout.dialog_win, null);
            this.<TextView>view(dialogView, R.id.txv_win_gold).setText(getString(R.string.you_win_gold, Utils.getGold(goldIndex)));
            builder.setNeutralButton("Меню", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    resetState();
                    showMenu();
                    dialog.cancel();
                }
            });
        }
        AlertDialog alertDialog = builder
                .setCancelable(false) // Не закрывать по клику вне диалога
                .setView(dialogView)
                .create();
        alertDialog.show();
//        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setStBackgroundColor(R.color.buttonBackground);
//        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(R.color.buttonBackground);
    }

    private void saveGame() {
        if (gameSaver.canSave(this)) {
            gameSaver.saveGame(this);
        } else {
            Toast.makeText(this, "Не удалось сохранить игру!", Toast.LENGTH_SHORT).show();
        }
        resetState();
        showMenu();
    }

    public void showNextQuestion() {
        Log.d("MY", "Show next question...");
        final QuestionFragment fragment;
        if ((fragment = fragment(QUESTION_TAG)) != null) {
            int questionIndex;
            do {
                questionIndex = RANDOM.nextInt(questions.size() - 1);
            } while (Utils.isUsedQuestion(usedQuestions, questionIndex));
            usedQuestions.add(questionIndex);
            fragment.showNextQuestion(questions, questionIndex);
        }
    }

    public void processWrongAnswer() {
        Log.d("MY", "Process wrong answer...");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View view = getLayoutInflater().inflate(R.layout.dialog_gameover, null);
        AlertDialog alertDialog = builder
                .setCancelable(false) // Не закрывать по клику вне диалога
                .setView(view)
                .setNeutralButton("Ок", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveRecord(false);
                        resetState();
                        showMenu();
                        dialog.cancel();
                    }
                })
                .create();
        int prize = Utils.getPrize(goldIndex);
        this.<TextView>view(view, R.id.txv_gameover).setText(getString(R.string.gameover_message, prize));
        alertDialog.show();
//        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setBackgroundColor(R.color.buttonBackground);
    }

    private void updateCurrentGold() {
        final InfoFragment fragment;
        if ((fragment = fragment(INFO_TAG)) != null) {
            fragment.updateCurrentGold(Utils.getGold(goldIndex));
        }
    }

    public void make50x50Help() {
        final VariantsFragment fragment;
        if ((fragment = fragment(VARIANTS_TAG)) != null) {
            fragment.make50x50Help();
            is50x50Used = true;
        }
    }

    public void makeZalHelp() {
        final VariantsFragment fragment;
        if ((fragment = fragment(VARIANTS_TAG)) != null) {
            final int[] helpPercents = fragment.getZalHelpPercents();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final View view = getLayoutInflater().inflate(R.layout.dialog_zalhelp, null);
            AlertDialog alertDialog = builder
                    .setCancelable(false) // Не закрывать по клику вне диалога
                    .setView(view)
                    .setNeutralButton("Ок", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .create();

            this.<TextView>view(view, R.id.txv_var_a).setText(getString(R.string.txv_var, helpPercents[0]));
            this.<TextView>view(view, R.id.txv_var_b).setText(getString(R.string.txv_var, helpPercents[1]));
            final TextView varC = view(view, R.id.txv_var_c);
            final TextView varD = view(view, R.id.txv_var_d);
            if (helpPercents.length == 4) {
                varC.setText(getString(R.string.txv_var, helpPercents[2]));
                varD.setText(getString(R.string.txv_var, helpPercents[3]));
            } else {
                varC.setVisibility(View.INVISIBLE);
                varD.setVisibility(View.INVISIBLE);
            }
            alertDialog.show();
//            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setBackgroundColor(R.color.buttonBackground);
            isZalUsed = true;
        }
    }

    public void makeCallHelp() {
        startActivity(new Intent(Intent.ACTION_DIAL, null));
        isCallUsed = true;
    }

    private void saveRecord(boolean keepPrize) {
        if (recordsSaver.canSave(this) ) {
            recordsSaver.saveRecords(this, keepPrize);
        } else {
            Toast.makeText(this, "Не удалось сохранить рекорд!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPutGameState(GameState gameState) {
        gameState.setSumIndex(goldIndex);
        gameState.setUserName(userName);
        gameState.setIs50x50Used(is50x50Used);
        gameState.setCallUsed(isCallUsed);
        gameState.setZalUsed(isZalUsed);
        int[] questions = ArrayUtils.toPrimitive(usedQuestions.toArray(new Integer[usedQuestions.size()]));
        gameState.setUsedQuestions(questions);
    }

    @Override
    public void onGameSaved() {
        Toast.makeText(this, "Игра сохранена!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetGameState(GameState gameState) {
        userName = gameState.getUserName();
        goldIndex = gameState.getSumIndex();
        is50x50Used = gameState.is50x50Used();
        isCallUsed = gameState.isCallUsed();
        isZalUsed = gameState.isZalUsed();
        usedQuestions = new ArrayList<>(Arrays.asList(ArrayUtils.toObject(gameState.getUsedQuestions())));
    }

    private void updateHelps() {
        final InfoFragment fragment;
        if ((fragment = fragment(INFO_TAG)) != null) {
            fragment.updateHelps(isZalUsed, isCallUsed, is50x50Used);
        }
    }

    @Override
    public void onGameLoaded() {
        startGame();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog alertDialog = builder
                .setCancelable(false) // Не закрывать по клику вне диалога
                .setView(getLayoutInflater().inflate(R.layout.dialog_game_loaded, null))
                .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateCurrentGold();
                        showNextQuestion();
                        updateHelps();
                        dialog.cancel();
                    }
                })
                .create();
        alertDialog.show();

    }

    @Override
    public void onPutRecords(StringBuilder records, boolean isKeepSum) {
        int currentPrize = isKeepSum ? Utils.getGold(goldIndex) : Utils.getPrize(goldIndex);
        Log.d("MY", "User: " + userName + "   prize: " + currentPrize);
        records.append(String.format(Locale.US, "-> [%s] %s $%d<br/>",
                new SimpleDateFormat("yyyy.MM.dd hh:mm:ss", Locale.US).format(GregorianCalendar.getInstance().getTime()),
                userName,
                currentPrize).replaceAll(" ", "&#160;"));
    }

    @Override
    public void onRecordsSaved() {

    }

    public RecordsSaver getRecordsSaver() {
        return recordsSaver;
    }
}
