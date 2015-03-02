package person; /**
 * Created by michael on 2/18/15.
 */

import request.Request;
import building.Building;
import core.Direction;

import java.util.Random;
public class Person implements Comparable<Person>{
    int id;
    private Request destination;
    int maxFloor = Building.getInstance().getNumFloors();


    public Person(int id)
    {
        this.id = id;
    }

    private int randomFloor(int floors) {
        int floor;
        Random rand = new Random();
        floor = (rand.nextInt(floors)) + 1;
        return floor;
    }

    public void setRequest() {
        int start;
        int end;

        start = randomFloor(maxFloor);
        end = randomFloor(maxFloor);

        /*
         * edge cases
         */
        if (start == end){
            if (start == maxFloor){
                start = randomFloor(maxFloor - 1);
            }
            else if (start == 1) {
                start = randomFloor(maxFloor - 1) + 1;
            }
            else {
                int result;
                Random coin = new Random();
                result = coin.nextInt(2);
                if (result == 0){
                    start = randomFloor(end - 1);
                } else {
                    start = (randomFloor(maxFloor - end)) + end;
                }
            }
        }
        destination = new Request(start, end);
    }

    public void requestElevator() {

    }

    public int compareTo(Person other) {
        if (this.getId() > other.getId()) { return 1; }
        if (this.getId() < other.getId()) { return -1; }
        return 0;
    }


    public int getStartFloor() {return destination.getStartFloor(); }
    public int getTargetFloor() {return destination.getTargetFloor(); }
    public Direction getDesiredDirection() {return destination.getDirection(); }
    public int getId() { return id; }
}
