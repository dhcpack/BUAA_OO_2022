import com.oocourse.uml3.models.elements.UmlInterface;

import java.util.HashSet;

public class MyInterface extends MyInterfaceClass {
    private final UmlInterface umlInterface;

    public MyInterface(UmlInterface umlInterface) {
        this.umlInterface = umlInterface;
        setUmlElement(umlInterface);
    }

    public void getAllRelatedInterfaces(HashSet<MyInterface> interfaces) {
        interfaces.add(this);
        if (getParents().size() != 0) {
            for (MyInterfaceClass interfaceClass : getParents()) {
                ((MyInterface) interfaceClass).getAllRelatedInterfaces(interfaces);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MyInterface that = (MyInterface) o;
        return this.umlInterface.equals(that.umlInterface);
    }

    @Override
    public int hashCode() {
        return this.umlInterface.hashCode();
    }
}
