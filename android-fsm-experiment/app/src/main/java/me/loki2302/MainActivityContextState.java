package me.loki2302;

import org.codehaus.jackson.annotate.JsonTypeInfo;

import me.loki2302.fsm.State;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "_type")
public interface MainActivityContextState extends State<MainActivityContext> {
}
