package me.loki2302.androidtestingexperiment;

import android.app.Activity;
import android.os.Bundle;

import java.util.concurrent.Exchanger;

public class OnPauseActivity extends Activity {
    public Exchanger<Void> onPauseExchanger = new Exchanger<Void>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_pause);
    }

    @Override
    protected void onPause() {
        super.onPause();

        try {
            onPauseExchanger.exchange(null);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
