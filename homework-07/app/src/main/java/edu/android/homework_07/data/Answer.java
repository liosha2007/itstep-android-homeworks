package edu.android.homework_07.data;


import java.io.Serializable;

/**
 * @author liosha on 10.06.2016.
 */
public class Answer implements Serializable {
    private boolean isCorrect = false;
    private String content;

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
