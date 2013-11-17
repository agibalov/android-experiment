package me.loki2302;

import me.loki2302.dal.RetaskApi;

import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class RetaskModule implements Module {
	@Override
	public void configure(Binder binder) {
	}
			
	@Provides
	@Singleton
	RetaskApi retaskApi() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		MappingJacksonHttpMessageConverter mappingJacksonHttpMessageConverter = new MappingJacksonHttpMessageConverter();
		mappingJacksonHttpMessageConverter.setObjectMapper(objectMapper);
		
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(mappingJacksonHttpMessageConverter);
		
		RetaskApi retaskApi = new RetaskApi(restTemplate, "http://retask.me/api");
		return retaskApi;
	}
}