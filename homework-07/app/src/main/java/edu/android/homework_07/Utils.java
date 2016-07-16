package edu.android.homework_07;

import android.content.Context;
import android.content.res.XmlResourceParser;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

import edu.android.homework_07.data.Answer;
import edu.android.homework_07.data.Question;

/**
 * @author liosha (02.07.2016).
 */
public class Utils {
    private static final int[] golds = {
            100, 200, 300, 500, 1000, 2000, 4000, 8000, 16000, 32000, 64000, 125000, 250000, 500000, 1000000
    };
    private static final int[] saveSums = {
            1000, 32000, 1000000
    };

    public static boolean isFinalPrize(int sumIndex) {
        return sumIndex == golds.length - 1;
    }

    public static int getGold(int index) {
        return golds[index];
    }

    public static boolean isUsedQuestion(List<Integer> usedQuestions, int questionIndex) {
        return usedQuestions.contains(questionIndex);
    }

    public static int getPrize(int goldIndex) {
        int currentSum = golds[goldIndex], prize = 0;
        for (int saveSum : saveSums) {
            if (currentSum >= saveSum && prize < saveSum) {
                prize = saveSum;
            }
        }
        return prize;
    }

    public static List<Question> loadQuestions(Context context, int resourceId) {
        List<Question> questions = new ArrayList<>();
        XmlResourceParser parser = context.getResources().getXml(resourceId);
        try {
            int eventType = parser.getEventType();
            Answer answer = null;
            Question question = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                final String tagName = parser.getName();
                if (eventType == XmlPullParser.START_TAG) {
                    if ("question".equals(tagName)) {
                        question = new Question();
                        question.setQuestion(parser.getAttributeValue(null, "text"));
                    } else if ("answer".equals(tagName)) {
                        answer = new Answer();
                        final String correct = parser.getAttributeValue(null, "correct");
                        answer.setIsCorrect("true".equalsIgnoreCase(correct));
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if ("question".equals(tagName)) {
                        questions.add(question);
                        question = null;
                    } else if ("answer".equals(tagName)) {
                        if (question != null) {
                            final Answer[] answers = question.getAnswers();
                            for (int n = 0; n < answers.length; n++) {
                                if (answers[n] == null) {
                                    answers[n] = answer;
                                    break;
                                }
                            }
                        }
                        answer = null;
                    }
                } else if (eventType == XmlPullParser.TEXT) {
                    if (answer != null) {
                        answer.setContent(parser.getText());
                    }
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return questions;
    }
}
