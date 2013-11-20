package me.loki2302;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

public class MainActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_view);
		
		ViewPager viewPager = (ViewPager)findViewById(R.id.viewPager);
		viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
			@Override
			public Fragment getItem(int arg0) {
				Bundle arguments = new Bundle();
				arguments.putString("title", String.format("Page #%d", arg0));
				arguments.putString("content", String.format("Content for page #%d", arg0));
				
				PageFragment pageFragment = new PageFragment();				
				pageFragment.setArguments(arguments);
				
				return pageFragment;
			}

			@Override
			public int getCount() {
				return 5;
			}			
		});
	}
}
