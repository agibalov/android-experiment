package me.loki2302.views;

import java.lang.reflect.Field;

import roboguice.inject.InjectView;
import android.view.View;
import android.view.ViewGroup;

public abstract class UiUtils {	
	public static void injectViews(ViewGroup root) {
		Field[] fields = root.getClass().getDeclaredFields();
        for(Field field : fields) {
            InjectView injectViewAnnotation = field.getAnnotation(InjectView.class);
            if(injectViewAnnotation == null) {
            	continue;
            }
            
            int id = injectViewAnnotation.value();
            View foundView = root.findViewById(id);
            field.setAccessible(true);
            try {
				field.set(root, foundView);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
        }
	}
}