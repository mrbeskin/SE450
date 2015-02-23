package building;

import elevator.Elevator;
import elevator.ElevatorImpl;

/**
 * building.ElevatorFactory is for creating instances of individual elevators.
 *
 * Created by michael on 2/8/15.
 */
public class ElevatorFactory {

    public static Elevator build(int elevatorID){

        return new ElevatorImpl(elevatorID, 1);

    }

}
