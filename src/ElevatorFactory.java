/**
 * ElevatorFactory is for creating instances of individual elevators.
 *
 * Created by michael on 2/8/15.
 */
public class ElevatorFactory {

    public Elevator build(int elevatorID){

        return new ElevatorImpl(elevatorID);

    }

}
