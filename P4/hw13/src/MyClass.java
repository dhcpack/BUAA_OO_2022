import com.oocourse.uml1.models.elements.UmlClass;

import java.util.ArrayList;
import java.util.List;

public class MyClass extends MyInterfaceClass {
    private final UmlClass umlClass;
    private final ArrayList<MyInterface> realizations = new ArrayList<>();

    public MyClass(UmlClass umlClass) {
        this.umlClass = umlClass;
    }

    public void addRealization(MyInterface myInterface) {
        realizations.add(myInterface);
    }

    public List<MyInterface> getRealizations() {
        return realizations;
        // return realizations.stream().map(MyInterface::getName).collect(Collectors.toList());
    }

    public int getSubClassCount() {
        return getChildren().size();
    }

    public int getOperationCount() {
        return getOperations().size();
    }

    public String getName() {
        return this.umlClass.getName();
    }

    public String getId() {
        return this.umlClass.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MyClass myClass = (MyClass) o;
        return this.umlClass.equals(myClass.umlClass);
    }

    @Override
    public int hashCode() {
        return this.umlClass.hashCode();
    }
}
