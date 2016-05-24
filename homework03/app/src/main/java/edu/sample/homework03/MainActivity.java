package edu.sample.homework03;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.GregorianCalendar;
import java.util.Random;

public class MainActivity extends GenericActivity {
    private static final int MAX_LINES_COUNT = 9;
    private static final int MAX_COLUMN_COUNT = 10;
    private static final Random RANDOM = new Random(GregorianCalendar.getInstance().getTimeInMillis());
    private TableLayout root;

    private final View.OnClickListener redClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            root.removeAllViews();
            Toast.makeText(MainActivity.this, R.string.game_over, Toast.LENGTH_SHORT).show();
        }
    };
    private final View.OnClickListener grnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            v.setBackgroundColor(Color.TRANSPARENT);
            v.setOnClickListener(null);
            createButtonPair();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(
                root = new TableLayout(this){{
                    setShrinkAllColumns(true);
                }}, new TableLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                )
        );

        fillContainer();
        createButtonPair();
    }

    private void fillContainer() {
        root.removeAllViews();
        for (int n = 0; n < MAX_LINES_COUNT; n++) {
            final TableRow tableRow = new TableRow(this);
            for (int m = 0; m < MAX_COLUMN_COUNT; m++) {
                tableRow.addView(new Button(this) {{
                    setBackgroundColor(Color.TRANSPARENT);
                }});
            }
            root.addView(tableRow);
        }
    }

    private void createButtonPair() {
        final Button redButton = new Button(this) {{
            setBackgroundColor(Color.RED);
        }};
        final int rowRed = RANDOM.nextInt(root.getChildCount());
        final View childRed = root.getChildAt(rowRed);
        if (childRed instanceof TableRow) {
            final TableRow redRow = (TableRow) childRed;
            final int colRed = RANDOM.nextInt(redRow.getChildCount());
            addButtonToPosition(redRow, redButton, colRed);
            redButton.setOnClickListener(redClickListener);
        }

        final Button grnButton = new Button(this) {{
            setBackgroundColor(Color.GREEN);
        }};
        final int rowGrn = RANDOM.nextInt(root.getChildCount());
        final View childGrn = root.getChildAt(rowGrn);
        if (childGrn instanceof TableRow) {
            final TableRow grnRow = (TableRow) childGrn;
            final int colGrn = RANDOM.nextInt(grnRow.getChildCount());
            addButtonToPosition(grnRow, grnButton, colGrn);
            grnButton.setOnClickListener(grnClickListener);
        }
    }

    private void addButtonToPosition(TableRow parent, Button button, int column) {
        parent.addView(button, column);
    }
}
