package me.loki2302.ui;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class AContainer extends AElement {
	@JsonProperty("children")
	public List<AElement> children;

	@Override
	public <TResult> TResult accept(AElementVisitor<TResult> visitor) {			
		return visitor.visit(this);
	}
}