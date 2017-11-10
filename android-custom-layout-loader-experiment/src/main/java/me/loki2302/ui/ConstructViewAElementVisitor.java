package me.loki2302.ui;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ConstructViewAElementVisitor implements AElementVisitor<View> {
	private final Context context;
	
	public ConstructViewAElementVisitor(Context context) {
		this.context = context;
	}
	
	@Override
	public View visit(AContainer e) {
		LinearLayout linearLayout = new LinearLayout(context);
		linearLayout.setId(e.id);
		for(AElement childElement : e.children) {
			View childView = childElement.accept(this);
			linearLayout.addView(childView);
		}
		return linearLayout;
	}

	@Override
	public View visit(ALabel e) {
		TextView textView = new TextView(context);
		textView.setId(e.id);
		textView.setText(e.text);
		return textView;
	}

	@Override
	public View visit(AButton e) {
		Button button = new Button(context);
		button.setId(e.id);
		button.setText(e.text);
		return button;
	}		
}