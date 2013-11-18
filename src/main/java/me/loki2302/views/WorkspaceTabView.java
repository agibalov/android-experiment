package me.loki2302.views;

import roboguice.inject.InjectView;
import me.loki2302.R;
import android.content.Context;
import android.widget.FrameLayout;
import android.widget.TextView;

public class WorkspaceTabView extends FrameLayout {
	@InjectView(R.id.tabName)
	private TextView tabNameTextView;
	
	public WorkspaceTabView(Context context) {
		super(context);
		inflate(context, R.layout.workspace_tab_view, this);
		UiUtils.injectViews(this);
	}
	
	public void setTabName(String tabName) {
		tabNameTextView.setText(tabName);
	}		
}