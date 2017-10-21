package f10.net.androidtoolbox.eventDriven;

import java.util.Observable;

/**
 * Created by fl0 on 18/10/2017.
 */

public class StateMachine {

    public interface StateMachineControl
    {
        boolean transitionIsAllowed(State from, State to);
        void onTransition(State from, State to);
    }

    public enum State {}

    private StateMachineControl _control;
    public ObservableValue<State> stateObservable;

    public StateMachine(StateMachineControl _control) {
        this._control = _control;
    }


    protected void moveToState(State toState)
    {
        if (_control.transitionIsAllowed(stateObservable.getValue(), toState))
        {
            _control.onTransition(stateObservable.getValue(), toState);
            stateObservable.setValue(toState);
        }
    }



}
