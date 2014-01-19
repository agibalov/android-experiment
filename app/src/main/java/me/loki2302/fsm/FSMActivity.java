package me.loki2302.fsm;

import android.support.v7.app.ActionBarActivity;

public abstract class FSMActivity<TContext> extends ActionBarActivity {
    private final StateService stateService = new StateService(this);
    private StateMachine<TContext> stateMachine;

    @Override
    protected void onPause() {
        super.onPause();

        State currentState = stateMachine.getState();
        stateService.saveState(getStateClass(), currentState, getLogicalInstanceId());
        currentState.unload(this);
        stateMachine = null;
    }

    @Override
    protected void onResume() {
        super.onResume();

        State<TContext> state = stateService.loadState(getStateClass(), getLogicalInstanceId());
        if(state == null) {
            state = getDefaultState();
        }

        stateMachine = new StateMachine<TContext>();
        stateMachine.setState((TContext)this, state);
    }

    protected void processEvent(Event event) {
        stateMachine.processEvent((TContext)this, event);
    }

    protected abstract Class<? extends State<TContext>> getStateClass();
    protected abstract State<TContext> getDefaultState();
    protected abstract int getLogicalInstanceId();
}
