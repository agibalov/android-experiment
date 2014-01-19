package me.loki2302.states;

import me.loki2302.MainActivityContextState;
import me.loki2302.MainActivityContext;
import me.loki2302.events.DisplayErrorMessageRequestedEvent;
import me.loki2302.events.DisplayProgressDialogRequestedEvent;
import me.loki2302.fsm.Event;
import me.loki2302.fsm.State;

public class DoingNothingState implements MainActivityContextState {
    @Override
    public void load(MainActivityContext mainActivityContext) {
    }

    @Override
    public void unload(MainActivityContext mainActivityContext) {
    }

    @Override
    public State processEvent(Event event) {
        if(event instanceof DisplayProgressDialogRequestedEvent) {
            return new DisplayingProgressDialogState();
        }

        if(event instanceof DisplayErrorMessageRequestedEvent) {
            return new DisplayingErrorMessageState();
        }

        throw new IllegalArgumentException("Unknown event type");
    }

    @Override
    public String describe() {
        return "Doing nothing";
    }
}
