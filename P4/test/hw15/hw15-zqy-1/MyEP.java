import com.oocourse.uml3.models.elements.UmlEndpoint;

public class MyEP extends MyLLep {
    private final UmlEndpoint uep;

    public MyEP(UmlEndpoint uep) {
        this.uep = uep;
    }

    public UmlEndpoint getUep() {
        return uep;
    }
}
