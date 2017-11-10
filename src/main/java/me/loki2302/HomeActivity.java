package me.loki2302;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory;
import android.view.View;
import android.widget.TextView;

public class HomeActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		
		layoutInflater.setFactory(new Factory() {
			@Override
			public View onCreateView(String name, Context context, AttributeSet attrs) {
				Log.i("X", String.format("onCreateView: %s", name));
				
				if(name.equals("TextView")) {
					TypedArray myLabelAttributesArray = context.obtainStyledAttributes(attrs, R.styleable.MyLabel);
					String extraText = null;
					try {
						extraText = myLabelAttributesArray.getString(R.styleable.MyLabel_extra_text);
					} finally {
						myLabelAttributesArray.recycle();
					}
					
					TextView textView = new TextView(context, attrs);
					if(extraText != null) {
						textView.setText(String.format("%s (%s)", textView.getText(), extraText));
					}
					
					return textView;
				}
				
				if(name.equals("MyLabel")) {
					int attributeCount = attrs.getAttributeCount();
					for(int i = 0; i < attributeCount; ++i) {
						String attributeName = attrs.getAttributeName(i);
						String attributeValue = attrs.getAttributeValue(i);
						Log.i("X", String.format("Attribute '%s' = '%s'", attributeName, attributeValue));
					}
					
					TypedArray myLabelAttributesArray = context.obtainStyledAttributes(attrs, R.styleable.MyLabel);
					String extraText = null;
					try {
						String customValue = myLabelAttributesArray.getString(R.styleable.MyLabel_extra_text);
						Log.i("X", String.format("READ CUSTOM AS '%s'", customValue));
						
						extraText = customValue;
					} finally {
						myLabelAttributesArray.recycle();
					}
					
					TextView textView = new TextView(context, attrs);
					textView.setTextColor(Color.rgb(255, 0, 0));
					if(extraText != null) {
						textView.setText(String.format("%s (%s)", textView.getText(), extraText));
					}
					
					return textView;
				}
				
				return null;
			}			
		});
		
		View rootView = layoutInflater.inflate(R.layout.home_view, null);
		setContentView(rootView);
	}
}