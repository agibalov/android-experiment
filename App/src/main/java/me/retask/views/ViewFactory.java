package me.retask.views;

import android.view.View;

public interface ViewFactory<T> {
	View makeView(T model);
}