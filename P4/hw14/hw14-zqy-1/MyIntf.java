import java.util.HashSet;
import com.oocourse.uml2.models.elements.UmlInterface;

public class MyIntf extends MyInterCls {
    private final UmlInterface umlIntf;
    private final HashSet<String> realSrcs = new HashSet<>();

    public MyIntf(UmlInterface umlIntf) {
        this.umlIntf = umlIntf;
    }

    public UmlInterface getUmlIntf() {
        return umlIntf;
    }

    public HashSet<String> getRealSrcs() {
        return realSrcs;
    }
}
