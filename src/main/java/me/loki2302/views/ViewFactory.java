package me.loki2302.views;

import android.view.View;

public interface ViewFactory<T> {
	View makeView(T model);
}