package edu.android.homework_15.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.android.homework_15.MainActivity;
import edu.android.homework_15.R;

public class MenuFragment extends GenericFragment<MainActivity> implements View.OnClickListener {

    private Button btn_start;
    private Button btn_exit;

    public MenuFragment() {
        super(R.layout.fragment_menu);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        (btn_start = view(R.id.btn_start)).setOnClickListener(this);
        (btn_exit = view(R.id.btn_exit)).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                getCurrentActivity().onStartGame();
                break;
            case R.id.btn_exit:
                getCurrentActivity().finish();
                break;
        }
    }
}
