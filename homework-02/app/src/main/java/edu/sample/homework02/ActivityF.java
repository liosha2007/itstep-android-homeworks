package edu.sample.homework02;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import edu.sample.homework02.model.Answers;

public class ActivityF extends GenericActivity implements View.OnClickListener {

    private EditText edtAnswer;
    private Button btnStartTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f);

        edtAnswer = view(R.id.edt_answer);
        btnStartTest = view(R.id.btn_next_question);
        btnStartTest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next_question:
                onMyNextQuestionClicked();
                break;
        }
    }

    private void onMyNextQuestionClicked() {
        final String answer = edtAnswer.getText().toString();
        Answers.setAnswer(Answers.ANSWER6, answer);

        startActivity(new Intent(this, ActivitySummary.class));
    }
}
