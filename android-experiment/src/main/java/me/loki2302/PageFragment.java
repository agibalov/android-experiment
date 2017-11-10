package me.loki2302;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

public class PageFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {			
		FrameLayout pageView = new FrameLayout(getActivity());
		inflater.inflate(R.layout.page_view, pageView);
		
		Bundle arguments = getArguments();
		TextView pageTitleTextView = (TextView)pageView.findViewById(R.id.pageTitle);
		pageTitleTextView.setText(arguments.getString("title"));			
		
		TextView pageContentTextView = (TextView)pageView.findViewById(R.id.pageContent);
		pageContentTextView.setText(arguments.getString("content"));
		
		return pageView;
	}
}