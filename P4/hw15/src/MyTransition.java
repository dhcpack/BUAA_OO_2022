import com.oocourse.uml3.models.elements.UmlEvent;
import com.oocourse.uml3.models.elements.UmlTransition;

import java.util.ArrayList;

public class MyTransition {
    private final UmlTransition umlTransition;
    private final ArrayList<UmlEvent> events = new ArrayList<>();
    private final MyState sourceState;
    private final MyState targetState;

    public MyTransition(UmlTransition umlTransition, MyState sourceState, MyState targetState) {
        this.umlTransition = umlTransition;
        this.sourceState = sourceState;
        this.targetState = targetState;
    }

    public void addEvent(UmlEvent umlEvent) {
        this.events.add(umlEvent);
    }

    public ArrayList<UmlEvent> getEvents() {
        return this.events;
    }

    public String getTargetId() {
        return this.umlTransition.getTarget();
    }

    public MyState getTargetState() {
        return this.targetState;
    }

    public String getGuard() {
        return this.umlTransition.getGuard();
    }
}
