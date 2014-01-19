package me.loki2302.fsm;

import android.util.Log;

import me.loki2302.MainActivityContext;

public class StateMachine<TContext> {
    private State<TContext> state;

    public void setState(TContext context, State<TContext> newState) {
        changeState(context, newState);
    }

    public State<TContext> getState() {
        return state;
    }

    public void processEvent(TContext context, Event event) {
        if(state == null) {
            throw new IllegalStateException("State is not set");
        }

        State<TContext> newState = state.processEvent(event);
        changeState(context, newState);
    }

    private void changeState(TContext context, State<TContext> newState) {
        Log.i("AAA", String.format("Current state is \"%s\"", state));
        if(state != null) {
            state.unload(context);
        }

        Log.i("AAA", String.format("New state is \"%s\"", newState));

        state = newState;
        state.load(context);

        onStateChanged(context, state);
    }

    private void onStateChanged(TContext context, State<TContext> newState) {
        ((MainActivityContext)context).trace(newState.describe());
    }
}
