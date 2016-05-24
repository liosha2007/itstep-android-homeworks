package edu.sample.homework02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import edu.sample.homework02.model.Answers;

public class ActivitySummary extends GenericActivity {

    private TextView txvSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        txvSummary = view(R.id.txv_summary);

        StringBuilder builder = new StringBuilder();
        builder.append(string(R.string.question_1)).append("\n\t")
                .append(Answers.getAnswer(Answers.ANSWER1)).append("\n\n");
        builder.append(string(R.string.question_2)).append("\n\t")
                .append(Answers.getAnswer(Answers.ANSWER2)).append("\n\n");
        builder.append(string(R.string.question_3)).append("\n\t")
                .append(Answers.getAnswer(Answers.ANSWER3)).append("\n\n");
        builder.append(string(R.string.question_4)).append("\n\t")
                .append(Answers.getAnswer(Answers.ANSWER4)).append("\n\n");
        builder.append(string(R.string.question_5)).append("\n\t")
                .append(Answers.getAnswer(Answers.ANSWER5)).append("\n\n");
        builder.append(string(R.string.question_6)).append("\n\t")
                .append(Answers.getAnswer(Answers.ANSWER6)).append("\n\n");

        txvSummary.setText(builder.toString());
    }
}
