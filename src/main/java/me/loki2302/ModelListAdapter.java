package me.loki2302;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


public class ModelListAdapter<T> extends BaseAdapter {
	private final List<T> items;
	private final ViewFactory<T> viewFactory;
	
	public ModelListAdapter(List<T> items, ViewFactory<T> viewFactory) {
		this.items = items;
		this.viewFactory = viewFactory;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {			
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		T item = items.get(position);
		View view = viewFactory.makeView(item);
		return view;
	}				
}