package edu.android.homework_07.data;

import java.io.Serializable;

/**
 * @author liosha on 24.06.2016.
 */
public class GameState implements Serializable {
    private int sumIndex;
    private int[] usedQuestions;
    private String userName;
    private boolean is50x50Used;
    private boolean isCallUsed;
    private boolean isZalUsed;

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

    public boolean is50x50Used() {
        return is50x50Used;
    }

    public void setIs50x50Used(boolean is50x50Used) {
        this.is50x50Used = is50x50Used;
    }

    public boolean isCallUsed() {
        return isCallUsed;
    }

    public void setCallUsed(boolean callUsed) {
        isCallUsed = callUsed;
    }

    public boolean isZalUsed() {
        return isZalUsed;
    }

    public void setZalUsed(boolean zalUsed) {
        isZalUsed = zalUsed;
    }
}
