package me.loki2302.ui;


import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;

@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME, 
		include = JsonTypeInfo.As.PROPERTY, 
		property = "type")
@JsonSubTypes({ 
	@Type(value = AContainer.class, name = "container"),
	@Type(value = AButton.class, name = "button"),
	@Type(value = ALabel.class, name = "label") })
public abstract class AElement {		
	@JsonProperty("id")
	public int id;
	
	public abstract <TResult> TResult accept(AElementVisitor<TResult> visitor);
}