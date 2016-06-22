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

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

import edu.android.homework_07.R;
import edu.android.homework_07.data.Question;
import edu.android.homework_07.fragment.AboutFragment;
import edu.android.homework_07.fragment.InfoFragment;
import edu.android.homework_07.fragment.MenuFragment;
import edu.android.homework_07.fragment.QuestionFragment;
import edu.android.homework_07.fragment.RecordsFragment;
import edu.android.homework_07.fragment.VariantsFragment;

public class MainActivity extends GenericActivity {
    public static final String MENU_TAG = "MENU_TAG";
    public static final String INFO_TAG = "INFO_TAG";
    public static final String QUESTION_TAG = "QUESTION_TAG";
    public static final String VARIANTS_TAG = "VARIANTS_TAG";
    public static final String RECORDS_TAG = "RECORDS_TAG";
    public static final String ABOUT_TAG = "ABOUT_TAG";
    public static String RECORDS_FILE_PATH;
    //    private FrameLayout frl_top;
//    private FrameLayout frl_center;
//    private FrameLayout frl_bottom;
    private int goldIndex = 0;
    private final int[] golds = {
            100, 200, 300, 500, 1000, 2000, 4000, 8000, 16000, 32000, 64000, 125000, 250000, 500000, 1000000
    };
    private final int[] saveSums = {
            1000, 32000, 1000000
    };
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RECORDS_FILE_PATH = getBaseContext().getFilesDir().getAbsolutePath() + File.separator + "records.txt";
        prepareViews();
        showMenu();
    }

    private void showMenu() {
        removeFragmentsByTag(getFragmentManager(), INFO_TAG, QUESTION_TAG)
                .replace(R.id.frl_bottom, new MenuFragment(), MENU_TAG)
                .commit();
    }

    private void prepareViews() {
//        frl_top = view(R.id.frl_top);
//        frl_center = view(R.id.frl_center);
//        frl_bottom = view(R.id.frl_bottom);
    }

    public void loadGame() {

    }

    public void startNewGame() {
        resetState();
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
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frl_top, new InfoFragment(), INFO_TAG)
                                .replace(R.id.frl_bottom, new VariantsFragment(), VARIANTS_TAG)
                                .replace(R.id.frl_center, new QuestionFragment(), QUESTION_TAG)
                                .addToBackStack(null).commit();
                        dialog.cancel();
                    }
                })
                .create();
        alertDialog.show();
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
        goldIndex = 0;
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
        if (goldIndex < golds.length - 1) {
            updateCurrentGold();
            dialogView = getLayoutInflater().inflate(R.layout.dialog_correct, null);
            this.<TextView>view(dialogView, R.id.txv_got_gold).setText(getString(R.string.you_got_gold, golds[goldIndex]));
            builder.setNegativeButton("Сохранить и выйти", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    saveGame();
                    saveRecord(true);
                    resetState();
                    showMenu();
                }
            });
            builder.setPositiveButton(R.string.next_question, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (goldIndex != golds.length) {
                        goldIndex++;

                        showNextQuestion();
                    }
                    dialog.cancel();
                }
            });
        } else {
            saveRecord(true);
            dialogView = getLayoutInflater().inflate(R.layout.dialog_win, null);
            this.<TextView>view(dialogView, R.id.txv_win_gold).setText(getString(R.string.you_win_gold, golds[goldIndex]));
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
        // TODO: save data
    }

    private void showNextQuestion() {
        Log.d("MY", "Show next question...");
        final QuestionFragment fragment;
        if ((fragment = fragment(QUESTION_TAG)) != null) {
            fragment.showNextQuestion();
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
        int prize = calculatePrize();
        this.<TextView>view(view, R.id.txv_gameover).setText(getString(R.string.gameover_message, prize));
        alertDialog.show();
//        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setBackgroundColor(R.color.buttonBackground);
    }

    private int calculatePrize() {
        int currentSum = golds[goldIndex], prize = 0;
        for (int saveSum : saveSums) {
            if (currentSum >= saveSum && prize < saveSum) {
                prize = saveSum;
            }
        }
        return prize;
    }

    private void updateCurrentGold() {
        final InfoFragment fragment;
        if ((fragment = fragment(INFO_TAG)) != null) {
            fragment.updateCurrentGold(golds[goldIndex]);
        }
    }

    public void make50x50Help() {
        final VariantsFragment fragment;
        if ((fragment = fragment(VARIANTS_TAG)) != null) {
            fragment.make50x50Help();
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
        }
    }

    public void makeCallHelp() {
        startActivity(new Intent(Intent.ACTION_DIAL, null));
    }

    private void saveRecord(boolean keepPrize) {
        int currentPrize = keepPrize ? golds[goldIndex] : calculatePrize();
        Log.d("MY", "User: " + MainActivity.this.userName + "   prize: " + (keepPrize ? golds[goldIndex] : calculatePrize()));
        if (currentPrize > 0) {
            try {
                FileOutputStream internalFileOutputStream = new FileOutputStream(RECORDS_FILE_PATH, true);
                PrintWriter internalWriter = new PrintWriter(internalFileOutputStream);
                internalWriter.write(String.format("-> [%s] %s $%d<br/>",
                        new SimpleDateFormat("yyyy.MM.dd hh:mm:ss", Locale.US).format(GregorianCalendar.getInstance().getTime()),
                        userName,
                        currentPrize).replaceAll(" ", "&#160;"));
                internalWriter.close();
                internalFileOutputStream.close();
            } catch (Exception e) {
                Log.d("MY", "Can't save records! ", e);
            }
        }
    }
}
