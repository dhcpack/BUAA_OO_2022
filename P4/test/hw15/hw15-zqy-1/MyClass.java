import java.util.HashSet;
import com.oocourse.uml3.models.elements.UmlClass;
import com.oocourse.uml3.models.elements.UmlClassOrInterface;

public class MyClass extends MyInterCls {
    private final UmlClass umlClass;
    private final HashSet<String> ops = new HashSet<>();
    private final HashSet<String> realTgts = new HashSet<>();

    public MyClass(UmlClass umlClass) {
        this.umlClass = umlClass;
    }

    public UmlClassOrInterface getUml() {
        return umlClass;
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
