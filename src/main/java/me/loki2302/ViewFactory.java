package me.loki2302;

import android.view.View;

public interface ViewFactory<T> {
	View makeView(T model);
}