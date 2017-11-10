package me.loki2302.states;

import me.loki2302.MainActivityContext;
import me.loki2302.MainActivityContextState;
import me.loki2302.events.ErrorMessageDialogClosedEvent;
import me.loki2302.fsm.Event;
import me.loki2302.fsm.State;

public class DisplayingErrorMessageState implements MainActivityContextState {
    @Override
    public void load(MainActivityContext mainActivityContext) {
        mainActivityContext.displayErrorMessage();
    }

    @Override
    public void unload(MainActivityContext mainActivityContext) {
        mainActivityContext.hideErrorMessage();
    }

    @Override
    public State processEvent(Event event) {
        if(event instanceof ErrorMessageDialogClosedEvent) {
            return new DoingNothingState();
        }

        throw new IllegalArgumentException("Unknown event type");
    }

    @Override
    public String describe() {
        return "Displaying error message";
    }
}
