package me.loki2302.ui;

import java.io.IOException;
import java.io.InputStream;


import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import roboguice.inject.ContextSingleton;
import android.content.Context;
import android.view.View;

@ContextSingleton
public class LayoutLoader {
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	public View load(Context context, int rawResourceId) {
		InputStream inputStream = context.getResources().openRawResource(rawResourceId);			
		try {
			AElement rootElement = objectMapper.readValue(inputStream, AElement.class);				
			ConstructViewAElementVisitor visitor = new ConstructViewAElementVisitor(context);
			return rootElement.accept(visitor);				
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {}
		}
	}
}