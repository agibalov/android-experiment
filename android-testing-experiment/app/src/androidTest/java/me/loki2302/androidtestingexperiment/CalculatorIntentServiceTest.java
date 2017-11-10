package me.loki2302.androidtestingexperiment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.test.ServiceTestCase;

import java.util.concurrent.Exchanger;

public class CalculatorIntentServiceTest extends ServiceTestCase<CalculatorIntentService> {
    public CalculatorIntentServiceTest() {
        super(CalculatorIntentService.class);
    }

    public void testCanUseCalculatorTestService() throws InterruptedException {
        final Exchanger<Integer> resultExchanger = new Exchanger<Integer>();
        getContext().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                int result = bundle.getInt("result");

                try {
                    resultExchanger.exchange(result);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, new IntentFilter("me.loki2302.androidtestingexperiment.notification"));

        Intent intent = new Intent();
        intent.putExtra("numberA", 2);
        intent.putExtra("numberB", 3);
        intent.setClass(getContext(), CalculatorIntentService.class);
        startService(intent);

        int result = resultExchanger.exchange(0);
        assertEquals(5, result);
    }
}
