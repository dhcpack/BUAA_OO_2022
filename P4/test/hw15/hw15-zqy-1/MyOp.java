import java.util.HashSet;
import com.oocourse.uml3.models.elements.UmlOperation;

public class MyOp {
    private final UmlOperation umlOp;
    private final HashSet<String> paras = new HashSet<>();

    public MyOp(UmlOperation umlOp) {
        this.umlOp = umlOp;
    }

    public UmlOperation getUmlOp() {
        return umlOp;
    }

    public HashSet<String> getParas() {
        return paras;
    }
}
