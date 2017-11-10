package me.loki2302.states;

import me.loki2302.MainActivityContextState;
import me.loki2302.MainActivityContext;
import me.loki2302.events.ProgressDialogClosedEvent;
import me.loki2302.fsm.Event;
import me.loki2302.fsm.State;

public class DisplayingProgressDialogState implements MainActivityContextState {
    @Override
    public void load(MainActivityContext mainActivityContext) {
        mainActivityContext.displayProgressDialog();
    }

    @Override
    public void unload(MainActivityContext mainActivityContext) {
        mainActivityContext.hideProgressDialog();
    }

    @Override
    public State processEvent(Event event) {
        if(event instanceof ProgressDialogClosedEvent) {
            return new DoingNothingState();
        }

        throw new IllegalArgumentException("Unknown event type");
    }

    @Override
    public String describe() {
        return "Displaying progress dialog";
    }
}
