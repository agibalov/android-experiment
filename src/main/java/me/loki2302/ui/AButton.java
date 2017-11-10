package me.loki2302.ui;

import org.codehaus.jackson.annotate.JsonProperty;

public class AButton extends AElement {
	@JsonProperty("text")
	public String text;

	@Override
	public <TResult> TResult accept(AElementVisitor<TResult> visitor) {
		return visitor.visit(this);
	}
}