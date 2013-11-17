package me.loki2302;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.TextView;

public class WorkspaceTabView extends FrameLayout {
	private final TextView tabNameTextView;
	
	public WorkspaceTabView(Context context) {
		super(context);
		inflate(context, R.layout.workspace_tab_view, this);
		tabNameTextView = (TextView)findViewById(R.id.tabName);
	}
	
	public void setTabName(String tabName) {
		tabNameTextView.setText(tabName);
	}		
}