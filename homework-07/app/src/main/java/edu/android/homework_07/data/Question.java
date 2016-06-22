package edu.android.homework_07.data;

import java.io.Serializable;

/**
 * @author liosha on 10.06.2016.
 */
public class Question implements Serializable {
    private String question;
    private Answer[] answers = new Answer[4];

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Answer[] getAnswers() {
        return answers;
    }

    public void setAnswers(Answer[] answers) {
        this.answers = answers;
    }
}
