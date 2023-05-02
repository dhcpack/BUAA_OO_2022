import java.util.HashSet;
import com.oocourse.uml3.models.elements.UmlElement;

public class MyState {
    private final UmlElement uele;
    private final HashSet<String> srcTTs = new HashSet<>();
    private final HashSet<String> tgtTTs = new HashSet<>();

    public MyState(UmlElement uele) {
        this.uele = uele;
    }

    public UmlElement getUele() {
        return uele;
    }

    public HashSet<String> getSrcTTs() {
        return srcTTs;
    }

    public HashSet<String> getTgtTTs() {
        return tgtTTs;
    }
}
