import java.util.HashSet;
import com.oocourse.uml3.models.elements.UmlTransition;

public class MyTrans {
    private final UmlTransition utrans;
    private final HashSet<String> events = new HashSet<>();
    private final HashSet<String> behavs = new HashSet<>();

    public MyTrans(UmlTransition utrans) {
        this.utrans = utrans;
    }

    public UmlTransition getUtrans() {
        return utrans;
    }

    public HashSet<String> getEvents() {
        return events;
    }

    public HashSet<String> getBehavs() {
        return behavs;
    }
}
