import java.util.HashSet;
import com.oocourse.uml3.models.elements.UmlCollaboration;

public class MyCollab {
    private final UmlCollaboration umlCollab;
    private final HashSet<String> attrs = new HashSet<>();
    private final HashSet<String> inters = new HashSet<>();

    public MyCollab(UmlCollaboration umlCollab) {
        this.umlCollab = umlCollab;
    }

    public UmlCollaboration getUmlCollab() {
        return umlCollab;
    }

    public HashSet<String> getAttrs() {
        return attrs;
    }

    public HashSet<String> getInters() {
        return inters;
    }
}
