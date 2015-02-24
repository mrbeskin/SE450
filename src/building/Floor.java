package building;

import controller.ElevatorController;
import person.Person;
import person.Request;

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
            newCall(new Request(person.getStartFloor(), person.getTargetFloor()));

        }
    }

    private void newCall(Request request){
        ElevatorController.getInstance().sendNewCall(request);
    }

    public ArrayList<Person> elevatorArrival(){

        synchronized(personQueue){
            return personQueue;
        }

    }

    public void elevatorDepart(ArrayList<Person> remaining){
        synchronized(personQueue){
            personQueue = remaining;
        }
    }

    public void whoIsOnFloor() {
        for (int i = 0; i < personQueue.size(); i++){
            System.out.printf("person.Person %d reporting in! I'm on floor %d \n", personQueue.get(i).getId(), floorID);
        }
    }

    public void sendNewCall(Person person){

    }


}
