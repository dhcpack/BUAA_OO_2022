import com.oocourse.uml2.models.common.ElementType;
import com.oocourse.uml2.models.elements.UmlElement;

import java.util.ArrayList;
import java.util.Objects;

public class MyState {
    private final UmlElement umlState;
    private final ArrayList<MyTransition> transitions = new ArrayList<>();

    public MyState(UmlElement umlState) {
        this.umlState = umlState;
    }

    public void addTransition(MyTransition transition) {
        this.transitions.add(transition);
    }

    public ArrayList<MyTransition> getTransitions() {
        return this.transitions;
    }

    public String getName() {
        return this.umlState.getName();
    }

    public ElementType getType() {
        return umlState.getElementType();
    }

    public String getId() {
        return this.umlState.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MyState myState = (MyState) o;
        return Objects.equals(umlState, myState.umlState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(umlState);
    }
}
