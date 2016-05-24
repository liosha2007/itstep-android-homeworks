package edu.sample.homework02.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liosha on 16.04.2016.
 */
public class Answers {
    private final static String[] ANSWERS = new String[6];
    public static final int ANSWER1 = 0;
    public static final int ANSWER2 = 1;
    public static final int ANSWER3 = 2;
    public static final int ANSWER4 = 3;
    public static final int ANSWER5 = 4;
    public static final int ANSWER6 = 5;

    public static boolean setAnswer(int answerNumber, String answerText) {
        if (answerNumber < ANSWER1 || answerNumber > ANSWER6) {
            return false;
        }
        ANSWERS[answerNumber] = answerText;
        return true;
    }

    public static String getAnswer(int answerNumber) {
        return ANSWERS[answerNumber];
    }
}
