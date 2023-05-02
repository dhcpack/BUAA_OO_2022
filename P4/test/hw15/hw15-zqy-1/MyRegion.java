import java.util.HashSet;
import com.oocourse.uml3.models.elements.UmlRegion;

public class MyRegion {
    private final UmlRegion ureg;
    private final HashSet<String> states = new HashSet<>();

    public MyRegion(UmlRegion ureg) {
        this.ureg = ureg;
    }

    public UmlRegion getUreg() {
        return ureg;
    }

    public HashSet<String> getStates() {
        return states;
    }
}
