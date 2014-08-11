package me.loki2302.androidtestingexperiment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class AddNumbersActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_numbers);

        findViewById(R.id.addNumbersButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = Integer.parseInt(((EditText) findViewById(R.id.numberAEditText)).getText().toString());
                int b = Integer.parseInt(((EditText) findViewById(R.id.numberBEditText)).getText().toString());
                int result = a + b;
                ((TextView)findViewById(R.id.resultTextView)).setText(String.valueOf(result));
            }
        });
    }
}
