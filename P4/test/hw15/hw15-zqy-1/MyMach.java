import java.util.HashSet;
import com.oocourse.uml3.models.elements.UmlStateMachine;

public class MyMach {
    private final UmlStateMachine usm;
    private final HashSet<String> regions = new HashSet<>();
    private final HashSet<String> states = new HashSet<>();
    private String initialState = null;
    private boolean haveFinalState = false;
    private Boolean canReachFinal = null;

    public MyMach(UmlStateMachine usm) {
        this.usm = usm;
    }

    public UmlStateMachine getUsm() {
        return usm;
    }

    public HashSet<String> getRegions() {
        return regions;
    }

    public HashSet<String> getStates() {
        return states;
    }

    public String getInitialState() {
        return initialState;
    }

    public void setInitialState(String initialState) {
        this.initialState = initialState;
    }

    public boolean isHaveFinalState() {
        return haveFinalState;
    }

    public void setHaveFinalState(boolean haveFinalState) {
        this.haveFinalState = haveFinalState;
    }

    public Boolean getCanReachFinal() {
        return canReachFinal;
    }

    public void setCanReachFinal(Boolean canReachFinal) {
        this.canReachFinal = canReachFinal;
    }
}
