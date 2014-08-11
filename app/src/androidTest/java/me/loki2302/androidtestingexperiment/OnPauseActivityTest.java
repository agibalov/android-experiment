package me.loki2302.androidtestingexperiment;

import android.accessibilityservice.AccessibilityService;
import android.test.ActivityInstrumentationTestCase2;

public class OnPauseActivityTest extends ActivityInstrumentationTestCase2<OnPauseActivity> {
    private OnPauseActivity activity;

    public OnPauseActivityTest() {
        super(OnPauseActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(false);
        activity = getActivity();
    }

    public void testDummy() throws InterruptedException {
        getInstrumentation().getUiAutomation().performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
        activity.onPauseExchanger.exchange(null);
        assertTrue(true);
    }
}
