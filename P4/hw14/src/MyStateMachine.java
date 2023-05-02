import com.oocourse.uml2.models.elements.UmlStateMachine;

public class MyStateMachine {
    private final UmlStateMachine umlStateMachine;
    private MyRegion region;

    public MyStateMachine(UmlStateMachine umlStateMachine) {
        this.umlStateMachine = umlStateMachine;
    }

    public void setRegion(MyRegion region) {
        this.region = region;
    }

    public MyRegion getRegion() {
        return this.region;
    }

}
