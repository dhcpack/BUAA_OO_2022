import com.oocourse.uml3.models.elements.UmlLifeline;

public class MyLL extends MyLLep {
    private final UmlLifeline ull;

    public MyLL(UmlLifeline ull) {
        this.ull = ull;
    }

    public UmlLifeline getUll() {
        return ull;
    }
}
