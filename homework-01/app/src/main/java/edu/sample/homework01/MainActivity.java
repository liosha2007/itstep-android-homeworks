package edu.sample.homework01;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnTextOne;
    private Button btnTextTwo;
    private Button btnTextThree;

    private Button btnToastOne;
    private Button btnToastTwo;
    private Button btnToastThree;

    private TextView txvText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTextOne = (Button) findViewById(R.id.btnTextOne);
        btnTextTwo = (Button) findViewById(R.id.btnTextTwo);
        btnTextThree = (Button) findViewById(R.id.btnTextThree);

        btnToastOne = (Button) findViewById(R.id.btnToastOne);
        btnToastTwo = (Button) findViewById(R.id.btnToastTwo);
        btnToastThree = (Button) findViewById(R.id.btnToastThree);

        txvText = (TextView) findViewById(R.id.txvText);

        btnTextOne.setOnClickListener(this);
        btnTextTwo.setOnClickListener(this);
        btnTextThree.setOnClickListener(this);

        btnToastOne.setOnClickListener(this);
        btnToastTwo.setOnClickListener(this);
        btnToastThree.setOnClickListener(this);

        txvText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        int stringId = 0;
        boolean isToast = true;
        switch (id) {
            case R.id.btnTextOne:
                stringId = R.string.text_text_1;
                isToast = false;
                break;
            case R.id.btnTextTwo:
                stringId = R.string.text_text_2;
                isToast = false;
                break;
            case R.id.btnTextThree:
                stringId = R.string.text_text_3;
                isToast = false;
                break;

            case R.id.btnToastOne:
                stringId = R.string.toast_text_1;
                break;
            case R.id.btnToastTwo:
                stringId = R.string.toast_text_2;
                break;
            case R.id.btnToastThree:
                stringId = R.string.toast_text_3;
                break;

            case R.id.txvText:
                txvText.setText("");
                break;
        }
        if (stringId != 0) {
            if (isToast) {
                Toast.makeText(this, stringId, Toast.LENGTH_LONG).show();;
            } else  {
                txvText.setText(stringId);
            }
        }
    }
}
