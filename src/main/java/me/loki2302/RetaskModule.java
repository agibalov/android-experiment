package me.loki2302;

import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

public class RetaskModule implements Module {
	@Override
	public void configure(Binder binder) {
	}
	
	@Provides
	@Singleton
	@Named("apiRootUrl")
	String apiRootUrl() {
		return "http://retask.me/api";
	}
			
	@Provides
	@Singleton
	RestTemplate restTemplate() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		MappingJacksonHttpMessageConverter mappingJacksonHttpMessageConverter = new MappingJacksonHttpMessageConverter();
		mappingJacksonHttpMessageConverter.setObjectMapper(objectMapper);
		
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(mappingJacksonHttpMessageConverter);
		
		return restTemplate;
	}
}