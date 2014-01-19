package me.loki2302.fsm;

public interface State<TContext> {
    void load(TContext context);
    void unload(TContext context);
    State processEvent(Event event);
    String describe();
}
