package elevator;

import elevator.Elevator;
import elevator.ElevatorImpl;

import java.util.concurrent.CountDownLatch;

/**
 * elevator.ElevatorFactory is for creating instances of individual elevators.
 *
 * Created by michael on 2/8/15.
 */
public class ElevatorFactory {

    public static Elevator build(int elevatorID, CountDownLatch startSignal, CountDownLatch doneSingal){

        return new ElevatorImpl(elevatorID, startSignal, doneSingal);

    }

}
