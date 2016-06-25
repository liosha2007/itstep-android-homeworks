package edu.android.homework_07.data;

import java.io.Serializable;

/**
 * @author liosha on 24.06.2016.
 */
public class GameState implements Serializable {
    private int sumIndex;
    private int usedIndex = 0;
    private int[] usedQuestions;
    private String userName;

    public int getSumIndex() {
        return sumIndex;
    }

    public void setSumIndex(int sumIndex) {
        this.sumIndex = sumIndex;
    }

    public int[] getUsedQuestions() {
        return usedQuestions;
    }

    public void setUsedQuestions(int[] usedQuestions) {
        this.usedQuestions = usedQuestions;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void addUsedQuestionIndex(int questionIndex) {
        usedQuestions[usedIndex++] = questionIndex;
    }

    public int getUsedIndex() {
        return usedIndex;
    }

    public void setUsedIndex(int usedIndex) {
        this.usedIndex = usedIndex;
    }
}
