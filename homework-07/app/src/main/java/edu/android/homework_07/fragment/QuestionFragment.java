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

    private TextView txv_question;

    public QuestionFragment() {
        super(R.layout.fragment_question);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txv_question = view(R.id.txv_question);
    }

    public void showNextQuestion(List<Question> questions, int questionIndex) {
        final Question question = questions.get(questionIndex);
        txv_question.setText(question.getQuestion());
        getCurrentActivity().showVariants(question);
    }


}
