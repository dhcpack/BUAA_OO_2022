import java.util.HashSet;
import com.oocourse.uml3.models.elements.UmlClassOrInterface;

public abstract class MyInterCls {
    private final HashSet<String> attrs = new HashSet<>();
    private final HashSet<String> genTgts = new HashSet<>();
    private final HashSet<String> genSrcs = new HashSet<>();
    private final HashSet<String> assocOtherEnds = new HashSet<>();

    public UmlClassOrInterface getUml() {
        return null;
    }

    public HashSet<String> getAssocOtherEnds() {
        return assocOtherEnds;
    }

    public HashSet<String> getAttrs() {
        return attrs;
    }

    public HashSet<String> getGenTgts() {
        return genTgts;
    }

    public HashSet<String> getGenSrcs() {
        return genSrcs;
    }
}
