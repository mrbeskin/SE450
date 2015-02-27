package building;

import controller.ElevatorController;
import person.Person;
import request.Request;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by michael on 2/8/15.
 */
public class Floor {

    private int floorID;
    private boolean upPressed;
    private boolean downPressed;
    private volatile CopyOnWriteArrayList<Person> personQueue = new CopyOnWriteArrayList<Person>();


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

    public CopyOnWriteArrayList<Person> elevatorArrival(){

        synchronized(personQueue){
            return personQueue;
        }

    }

    public void elevatorDepart(CopyOnWriteArrayList<Person> remaining){
        synchronized(personQueue){
            personQueue = remaining;
            for (int i = 0; i < personQueue.size(); i ++){
                newCall(new Request(personQueue.get(i).getStartFloor(), personQueue.get(i).getTargetFloor()));
            }
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
