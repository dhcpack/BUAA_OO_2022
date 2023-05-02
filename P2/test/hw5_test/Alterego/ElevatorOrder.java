import java.util.ArrayList;
import com.oocourse.elevator1.PersonRequest;

public class ElevatorOrder {
    private int type;   //0 move 3 stop
    private ArrayList<PersonRequest> personRequests;
    private PersonRequest majorRequest;
    private int destination;

    public void setPersonRequests(ArrayList<PersonRequest> personRequests) {
        this.personRequests = personRequests;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setMajorRequest(PersonRequest majorRequest) {
        this.majorRequest = majorRequest;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public int getDestination() {
        return this.destination;
    }

    public PersonRequest getMajorRequest() {
        return this.majorRequest;
    }

    public int getType() {
        return type;
    }

    public ArrayList<PersonRequest> getPersonRequests() {
        return personRequests;
    }
}
