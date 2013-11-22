package me.loki2302;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HomeActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_view);
		
		Button runViewPagerWithFragmentsButton = (Button)findViewById(R.id.runViewPagerWithFragmentsButton);
		runViewPagerWithFragmentsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(HomeActivity.this, ViewPagerWithFragmentsActivity.class);
				startActivity(intent);
			}
		});
		
		Button runViewPagerWithViewsButton = (Button)findViewById(R.id.runViewPagerWithViewsButton);
		runViewPagerWithViewsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(HomeActivity.this, ViewPagerWithViewsActivity.class);
				startActivity(intent);
			}
		});
	}	
}