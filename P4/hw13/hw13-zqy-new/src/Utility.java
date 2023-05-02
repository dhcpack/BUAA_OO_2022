import com.oocourse.uml1.models.elements.UmlElement;

public class Utility {
    public static int elementClassLevel(UmlElement e) {
        switch (e.getElementType()) {
            // level 0
            case UML_CLASS:
            case UML_INTERFACE:
                return 0;
            // level 1
            case UML_ATTRIBUTE:
            case UML_OPERATION:
            case UML_GENERALIZATION:
            case UML_INTERFACE_REALIZATION:
                return 1;
            // level 2
            case UML_PARAMETER:
                return 2;
            // ignored
            case UML_ASSOCIATION:
            case UML_ASSOCIATION_END:
            default:
                return 100;
        }
    }
}
