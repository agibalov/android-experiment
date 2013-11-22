package me.loki2302;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

public class ViewPagerWithViewsActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpager_with_views_view);
		
		ViewPager viewPager = (ViewPager)findViewById(R.id.viewPager);
		viewPager.setAdapter(new PagerAdapter() {
			@Override
			public void destroyItem(ViewGroup container, int position, Object object) {
				container.removeView((View)object);
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				FrameLayout pageView = new FrameLayout(ViewPagerWithViewsActivity.this);
				inflater.inflate(R.layout.page_view, pageView);
								
				TextView pageTitleTextView = (TextView)pageView.findViewById(R.id.pageTitle);
				String title = String.format("Page #%d", position);
				pageTitleTextView.setText(title);			
				
				TextView pageContentTextView = (TextView)pageView.findViewById(R.id.pageContent);
				String content = String.format("Content #%d", position);
				pageContentTextView.setText(content);
				
				container.addView(pageView);
				
				return pageView; // TODO: this may not be view
			}

			@Override
			public int getCount() {
				return 5;
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0.equals(arg1);
			}			
		});
	}
}
