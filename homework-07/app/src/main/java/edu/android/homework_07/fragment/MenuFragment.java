package edu.android.homework_07.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.android.homework_07.R;
import edu.android.homework_07.activity.MainActivity;

/**
 * @author liosha on 09.06.2016.
 */
public class MenuFragment extends GenericFragment<MainActivity> implements View.OnClickListener {
    private Button btn_load;
//    private Button btn_new;
//    private Button btn_records;
//    private Button btn_about;
//    private Button btn_exit;

    public MenuFragment() {
        super(R.layout.fragment_menu);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prepareViews();
    }

    private void prepareViews() {
        btn_load = view(R.id.btn_load);
        this.<Button>view(R.id.btn_new).setOnClickListener(this);
        this.<Button>view(R.id.btn_records).setOnClickListener(this);
        this.<Button>view(R.id.btn_about).setOnClickListener(this);
        this.<Button>view(R.id.btn_exit).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_load:
                getCurrentActivity().loadGame();
                break;
            case R.id.btn_new:
                getCurrentActivity().startNewGame();
                break;
            case R.id.btn_records:
                getCurrentActivity().showRecords();
                break;
            case R.id.btn_about:
                getCurrentActivity().showAbout();
                break;
            case R.id.btn_exit:
                getCurrentActivity().exit();
                break;
        }
    }
}
