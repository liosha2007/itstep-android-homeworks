package edu.android.homework_07.fragment;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import edu.android.homework_07.R;
import edu.android.homework_07.activity.MainActivity;
import edu.android.homework_07.data.Answer;
import edu.android.homework_07.data.Question;

/**
 * @author liosha on 09.06.2016.
 */
public class QuestionFragment extends GenericFragment<MainActivity> {
    public static final Random RANDOM = new Random(new Date().getTime());
    private List<Question> questions = null;

    private TextView txv_question;

    public QuestionFragment() {
        super(R.layout.fragment_question);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txv_question = view(R.id.txv_question);

        this.questions = getQuestions(getActivity(), R.xml.questions);

        showNextQuestion();
    }

    public void showNextQuestion() {
        final int questionIndex = RANDOM.nextInt(questions.size() - 1);
        final Question question = questions.remove(questionIndex);
        txv_question.setText(question.getQuestion());
        getCurrentActivity().showVariants(question);
    }


    public List<Question> getQuestions(Context context, int resourceId) {
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
