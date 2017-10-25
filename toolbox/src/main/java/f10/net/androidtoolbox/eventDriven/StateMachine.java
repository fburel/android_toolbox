package f10.net.androidtoolbox.eventDriven;

import java.util.Observable;

/**
 * Created by fl0 on 18/10/2017.
 */

public class StateMachine {

    public interface StateMachineControl
    {
        boolean transitionIsAllowed(int from, int to);
        void onTransition(int from, int to);
    }


    private StateMachineControl _control;
    public ObservableValue<Integer> stateObservable;


    public StateMachine(StateMachineControl _control, int intialState) {
        this._control = _control;
        this.stateObservable = new ObservableValue<Integer>(intialState);
    }


    public void moveToState(int toState)
    {
        if (_control.transitionIsAllowed(stateObservable.getValue(), toState))
        {
            _control.onTransition(stateObservable.getValue(), toState);
            stateObservable.setValue(toState);
        }
    }
}
