package me.loki2302.androidtestingexperiment;

import android.app.IntentService;
import android.content.Intent;

public class CalculatorIntentService extends IntentService {
    public CalculatorIntentService() {
        super("CalculatorIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int numberA = intent.getIntExtra("numberA", 0);
        int numberB = intent.getIntExtra("numberB", 0);
        int result = numberA + numberB;

        Intent resultIntent = new Intent("me.loki2302.androidtestingexperiment.notification");
        resultIntent.putExtra("result", result);
        sendBroadcast(resultIntent);
    }
}
