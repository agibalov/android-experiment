package me.loki2302.androidtestingexperiment;

import android.accessibilityservice.AccessibilityService;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.UiThreadTest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddNumbersActivityTest extends ActivityInstrumentationTestCase2<AddNumbersActivity> {
    private AddNumbersActivity activity;
    private EditText numberAEditText;
    private EditText numberBEditText;
    private Button addNumbersButton;
    private TextView resultTextView;

    public AddNumbersActivityTest() {
        super(AddNumbersActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(false);

        activity = getActivity();
        numberAEditText = (EditText)activity.findViewById(R.id.numberAEditText);
        numberBEditText = (EditText)activity.findViewById(R.id.numberBEditText);
        addNumbersButton = (Button)activity.findViewById(R.id.addNumbersButton);
        resultTextView = (TextView)activity.findViewById(R.id.resultTextView);
    }

    public void testAddNumbersWithTouchUtils() {
        TouchUtils.clickView(this, numberAEditText);
        sendKeys("2");

        TouchUtils.clickView(this, numberBEditText);
        sendKeys("3");

        TouchUtils.clickView(this, addNumbersButton);

        String resultText = resultTextView.getText().toString();
        assertEquals("5", resultText);
    }

    public void testAddNumbersWithRunOnUiThread() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                numberAEditText.setText("2");
            }
        });

        getInstrumentation().waitForIdleSync();

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                numberBEditText.setText("3");
            }
        });

        getInstrumentation().waitForIdleSync();

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                addNumbersButton.performClick();
            }
        });

        getInstrumentation().waitForIdleSync();

        assertEquals("5", resultTextView.getText().toString());
    }

    @UiThreadTest
    public void testAddNumbersWithUiThreadTest() {
        numberAEditText.setText("2");
        numberBEditText.setText("3");
        addNumbersButton.performClick();
        assertEquals("5", resultTextView.getText().toString());
    }
}
