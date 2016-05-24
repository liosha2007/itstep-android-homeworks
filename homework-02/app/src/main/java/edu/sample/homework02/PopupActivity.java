package edu.sample.homework02;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import edu.sample.homework02.model.Answers;

public class PopupActivity extends GenericActivity
        implements View.OnClickListener {

    private EditText edtAnswer;
    private Button btnInsert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        edtAnswer = view(R.id.edt_answer);
        btnInsert = view(R.id.btn_insert);
        btnInsert.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_insert:
                onMyNextQuestionClicked();
                break;
        }
    }

    private void onMyNextQuestionClicked() {
        Intent intent = getIntent();
        intent.putExtra("insert_text", edtAnswer.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }
}
