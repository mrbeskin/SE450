import java.util.ArrayList;

/**
 * Created by michael on 2/8/15.
 */
public class Floor {

    private int floorID;
    private boolean upPressed;
    private boolean downPressed;
    private volatile ArrayList<Person> personQueue = new ArrayList<Person>();


    public Floor(int floorNumber){
        floorID = floorNumber;

    }

    public int getID(){
        return floorID;
    }

    public void addPerson(Person person){
        synchronized (personQueue) {
            personQueue.add(person);
        }
    }

    public void elevatorArrival(int elevatorID){

        synchronized(personQueue){


        }

    }

    public void whoIsOnFloor() {
        for (int i = 0; i < personQueue.size(); i++){
            System.out.printf("Person %d reporting in! I'm on floor %d \n", personQueue.get(i).getId(), floorID);
        }
    }




}
