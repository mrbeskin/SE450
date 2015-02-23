package controller;

/**
 * Created by michael on 2/20/15.
 */
public class ElevatorCallFactory {
    public ElevatorCall build() {
        return new ElevatorCallImpl();
    }
}
