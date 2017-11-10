package me.loki2302;



import me.loki2302.ui.LayoutLoader;

import com.google.inject.Inject;

import roboguice.activity.RoboActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class HomeActivity extends RoboActivity {
	private final static int A_ROOT_CONTAINER = 23021;
	private final static int A_LABEL_1 = 23022;
	private final static int A_BUTTON_1 = 23023;
	
	@Inject
	private LayoutLoader layoutLoader;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(layoutLoader.load(this, R.raw.home_view));
		
		findViewById(A_BUTTON_1).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Toast.makeText(HomeActivity.this, "Clicked!", Toast.LENGTH_SHORT).show();
			}			
		});
	}
}