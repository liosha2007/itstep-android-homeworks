package edu.sample.homework02;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import edu.sample.homework02.model.Answers;

public class ActivityB extends GenericActivity
        implements View.OnClickListener {

    private EditText edtAnswer;
    private Button btnStartTest;
    private Button btnInsert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);

        edtAnswer = view(R.id.edt_answer);
        btnStartTest = view(R.id.btn_next_question);
        btnInsert = view(R.id.btn_insert);
        btnStartTest.setOnClickListener(this);
        btnInsert.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next_question:
                onMyNextQuestionClicked();
                break;
            case R.id.btn_insert:
                onMyInsertClicked();
                break;
        }
    }

    private void onMyInsertClicked() {
        startActivityForResult(new Intent(this, PopupActivity.class), 200);
    }

    private void onMyNextQuestionClicked() {
        final String answer = edtAnswer.getText().toString();
        Answers.setAnswer(Answers.ANSWER2, answer);

        startActivity(new Intent(this, ActivityC.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 200 && resultCode == RESULT_OK) {
            edtAnswer.setText(data.getStringExtra("insert_text"));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
