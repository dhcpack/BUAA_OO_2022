import com.oocourse.uml3.models.elements.UmlAttribute;
import com.oocourse.uml3.models.elements.UmlElement;

import java.util.ArrayList;

public class MyInterfaceClass {
    private UmlElement umlElement;
    private final ArrayList<MyInterfaceClass> children = new ArrayList<>();
    private final ArrayList<MyInterfaceClass> parents = new ArrayList<>();
    private final ArrayList<UmlAttribute> attributes = new ArrayList<>();
    private final ArrayList<MyOperation> operations = new ArrayList<>();
    private final ArrayList<String> associationEnd = new ArrayList<>();

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

    public void addAssociationEnd(String associationEnd) {
        this.associationEnd.add(associationEnd);
    }

    public ArrayList<String> getAssociationEnds() {
        return this.associationEnd;
    }

    public void setUmlElement(UmlElement umlElement) {
        this.umlElement = umlElement;
    }

    public UmlElement getUmlElement() {
        return this.umlElement;
    }

    public String getName() {
        return this.umlElement.getName();
    }

    public String getId() {
        return this.umlElement.getId();
    }
}
