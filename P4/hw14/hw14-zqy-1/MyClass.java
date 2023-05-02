import java.util.HashSet;
import com.oocourse.uml2.models.elements.UmlClass;

public class MyClass extends MyInterCls {
    private final UmlClass umlClass;
    private final HashSet<String> ops = new HashSet<>();
    private final HashSet<String> realTgts = new HashSet<>();

    public MyClass(UmlClass umlClass) {
        this.umlClass = umlClass;
    }

    public UmlClass getUmlClass() {
        return umlClass;
    }

    public HashSet<String> getOps() {
        return ops;
    }

    public HashSet<String> getRealTgts() {
        return realTgts;
    }
}
