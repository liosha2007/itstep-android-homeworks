package edu.android.homework_07.fragment;

import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import edu.android.homework_07.R;
import edu.android.homework_07.activity.MainActivity;
import edu.android.homework_07.data.Answer;
import edu.android.homework_07.data.Question;

/**
 * @author liosha on 09.06.2016.
 */
public class VariantsFragment extends GenericFragment<MainActivity> implements View.OnClickListener {
    private Button btn_question_one;
    private Button btn_question_two;
    private Button btn_question_three;
    private Button btn_question_four;

    public VariantsFragment() {
        super(R.layout.fragment_variants);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_question_one = view(R.id.btn_question_one);
        btn_question_two = view(R.id.btn_question_two);
        btn_question_three = view(R.id.btn_question_three);
        btn_question_four = view(R.id.btn_question_four);
    }

    public void showVariants(Question question) {
        final Answer[] answers = question.getAnswers();
        List<Button> buttons = new ArrayList<Button>(4){{
            add(btn_question_one);
            add(btn_question_two);
            add(btn_question_three);
            add(btn_question_four);
        }};
        Collections.shuffle(buttons, MainActivity.RANDOM);
        for (int n = 0; n < buttons.size(); n++) {
            final Answer answer = answers[n];
            final Button button = buttons.get(n);
            button.setBackgroundResource(R.drawable.block_small_background_empty);
            button.setText(answer.getContent());
            button.setTag(answer.isCorrect());
            button.setOnClickListener(this);
            button.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        final MainActivity currentActivity = getCurrentActivity();
        if (v.getTag() == true) {
            v.setBackgroundResource(R.drawable.block_small_background_correct_empty);
            currentActivity.processCorrectAnswer();
        } else {
            v.setBackgroundResource(R.drawable.block_small_background_wrong_empty);
            currentActivity.processWrongAnswer();
        }
    }

    public void make50x50Help() {
        List<Button> buttons = new ArrayList<Button>(4){{
            add(btn_question_one);
            add(btn_question_two);
            add(btn_question_three);
            add(btn_question_four);
        }};
        Collections.shuffle(buttons, MainActivity.RANDOM);

        int invisible = 0;
        for (int n = 0; n < buttons.size() && invisible < 2; n++) {
            final Button button = buttons.get(n);
            if (button.getTag() != true) {
                button.setVisibility(View.INVISIBLE);
                invisible++;
            }
        }
    }

    public int[] getZalHelpPercents() {
        List<Button> buttons = new ArrayList<Button>(4){{
            add(btn_question_one);
            add(btn_question_two);
            add(btn_question_three);
            add(btn_question_four);
        }};
        buttons = filterInvisible(buttons);
        int correctIndex = 0;
        for (int n = 0; n < buttons.size(); n++) {
            if (buttons.get(n).getTag() == true) {
                correctIndex = n;
                break;
            }
        }
        final int correction = 3;
        int max = 100 - correction;
        List<Integer> percentage = new ArrayList<>(buttons.size());
        while (percentage.size() < buttons.size() - 1) {
            final int nextInt = MainActivity.RANDOM.nextInt(max);
            max -= nextInt;
            percentage.add(nextInt);
        }
        percentage.add(100 - correction - sum(percentage));
        System.out.println("Variants: " + percentage);
        System.out.println("correctIndex: " + correctIndex);
        int maxIndex = percentage.indexOf(Collections.max(percentage));
        percentage.set(maxIndex, percentage.get(maxIndex) + correction);
        final int[] result = new int[buttons.size()];
        int curIndex = 0;
        for (int n = 0; n < result.length; n++) {
            if (n == correctIndex) {
                result[n] = percentage.get(maxIndex);
            } else {
                if (curIndex == maxIndex) {
                    curIndex++;
                }
                result[n] = percentage.get(curIndex);
                curIndex++;
            }

        }
        System.out.println("result: " + Arrays.toString(result));
        return result;
    }

    public Integer sum(List<Integer> list) {
        Integer sum= 0;
        for (Integer i:list)
            sum = sum + i;
        return sum;
    }

    public List<Button> filterInvisible(List<Button> target) {
        List<Button> result = new ArrayList<Button>();
        for (Button element: target) {
            if (element.getVisibility() == View.VISIBLE) {
                result.add(element);
            }
        }
        return result;
    }

}
