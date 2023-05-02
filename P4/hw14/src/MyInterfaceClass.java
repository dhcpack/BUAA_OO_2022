import com.oocourse.uml2.models.elements.UmlAttribute;

import java.util.ArrayList;

public class MyInterfaceClass {
    private final ArrayList<MyInterfaceClass> children = new ArrayList<>();
    private final ArrayList<MyInterfaceClass> parents = new ArrayList<>();
    private final ArrayList<UmlAttribute> attributes = new ArrayList<>();
    private final ArrayList<MyOperation> operations = new ArrayList<>();
    // private final ArrayList<UmlAssociationEnd> associationEnds = new ArrayList<>();

    public ArrayList<MyInterfaceClass> getChildren() {
        return children;
    }

    public void addChild(MyInterfaceClass child) {
        children.add(child);
    }

    public ArrayList<MyInterfaceClass> getParents() {
        return parents;
    }

    public void addParent(MyInterfaceClass parent) {
        parents.add(parent);
    }

    public void addAttribute(UmlAttribute attribute) {
        attributes.add(attribute);
    }

    public ArrayList<UmlAttribute> getAttributes() {
        return attributes;
    }

    public void addOperation(MyOperation operation) {
        operations.add(operation);
    }

    public ArrayList<MyOperation> getOperations() {
        return operations;
    }
}
