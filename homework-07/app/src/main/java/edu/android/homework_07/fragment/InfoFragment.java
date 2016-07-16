package edu.android.homework_07.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

import edu.android.homework_07.R;
import edu.android.homework_07.activity.MainActivity;

/**
 * @author liosha on 09.06.2016.
 */
public class InfoFragment extends GenericFragment<MainActivity> implements View.OnClickListener {
    private TextView txv_prize;
    private Button btn_50_50;
    private Button btn_zal;
    private Button btn_call;

    public InfoFragment() {
        super(R.layout.fragment_info);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prepareViews();
    }

    private void prepareViews() {
        (btn_50_50 = this.<Button>view(R.id.btn_50_50)).setOnClickListener(this);
        (btn_zal = this.<Button>view(R.id.btn_zal)).setOnClickListener(this);
        (btn_call = this.<Button>view(R.id.btn_call)).setOnClickListener(this);
        txv_prize = view(R.id.txv_prize);
    }

    @Override
    public void onClick(View v) {
        final MainActivity activity = getCurrentActivity();
        switch (v.getId()) {
            case R.id.btn_50_50:
                activity.make50x50Help();
                break;
            case R.id.btn_zal:
                activity.makeZalHelp();
                break;
            case R.id.btn_call:
                activity.makeCallHelp();
                break;
        }
        v.setVisibility(View.INVISIBLE);
    }

    public void updateCurrentGold(int gold) {
        txv_prize.setText(String.format(Locale.US, "$%d", gold));
    }

    public void updateHelps(boolean isZalUsed, boolean isCallUsed, boolean is50x50Used) {
        if (isZalUsed) {
            btn_zal.setVisibility(View.INVISIBLE);
        }
        if (isCallUsed) {
            btn_call.setVisibility(View.INVISIBLE);
        }
        if (is50x50Used) {
            btn_50_50.setVisibility(View.INVISIBLE);
        }
    }
}
