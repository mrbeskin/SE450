import java.util.concurrent.CountDownLatch;

/**
 * ElevatorFactory is for creating instances of individual elevators.
 *
 * Created by michael on 2/8/15.
 */
public class ElevatorFactory {

    public static Elevator build(int elevatorID, CountDownLatch startSignal, CountDownLatch doneSignal){

        return new ElevatorImpl(elevatorID, 1, startSignal, doneSignal);

    }

}
