package edu.android.homework_14.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import edu.android.homework_14.MainActivity;
import edu.android.homework_14.R;

public class MainFragment extends GenericFragment<MainActivity> implements View.OnClickListener {
    private ImageView imb_char;
    private ImageView imb_bench;

    public MainFragment() {
        super(R.layout.fragment_main);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        (imb_char = view(R.id.imb_char)).setOnClickListener(this);
        (imb_bench = view(R.id.imb_bench)).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imb_char:
                getCurrentActivity().openCharacteristics();
                break;
            case R.id.imb_bench:
                getCurrentActivity().openBenchmark();
                break;
        }
    }

}
