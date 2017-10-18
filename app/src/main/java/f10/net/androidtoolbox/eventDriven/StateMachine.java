package f10.net.androidtoolbox.eventDriven;

import java.util.Observable;

/**
 * Created by fl0 on 18/10/2017.
 */

public abstract class StateMachine {

    public enum State {}

    public class StateObservable extends ObservableObject {
        private State state;

        public State getState() {
            return state;
        }

        public void setState(State state) {
            notifyDataChanged();
            this.state = state;
        }
    }

    public StateObservable stateObservable;

    protected void moveToState(State toState)
    {
        if (transitionIsAllowed(stateObservable.getState(), toState))
        {
            onTransition(stateObservable.getState(), toState);
            stateObservable.setState(toState);
        }
    }

    protected abstract boolean transitionIsAllowed(State from, State to);
    protected abstract void onTransition(State from, State to);


}
