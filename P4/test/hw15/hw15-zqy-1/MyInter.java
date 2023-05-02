import java.util.HashSet;
import com.oocourse.uml3.models.elements.UmlInteraction;

public class MyInter {
    private final UmlInteraction umlInter;
    private final HashSet<String> endpoints = new HashSet<>();
    private final HashSet<String> lifelines = new HashSet<>();
    private final HashSet<String> msgs = new HashSet<>();

    public MyInter(UmlInteraction umlInter) {
        this.umlInter = umlInter;
    }

    public UmlInteraction getUmlInter() {
        return umlInter;
    }

    public HashSet<String> getEndpoints() {
        return endpoints;
    }

    public HashSet<String> getLifelines() {
        return lifelines;
    }

    public HashSet<String> getMsgs() {
        return msgs;
    }
}
