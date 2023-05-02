import com.oocourse.uml3.models.elements.UmlElement;

public class Util {
    public static int elementLevel(UmlElement e) {
        switch (e.getElementType()) {
            // level 0
            case UML_CLASS:
            case UML_INTERFACE:
            case UML_COLLABORATION:
            case UML_STATE_MACHINE:
                return 0;
            // level 1
            case UML_REGION:
            case UML_ATTRIBUTE:
            case UML_OPERATION:
            case UML_INTERACTION:
            case UML_GENERALIZATION:
            case UML_INTERFACE_REALIZATION:
                return 1;
            // level 2
            case UML_STATE:
            case UML_ENDPOINT:
            case UML_LIFELINE:
            case UML_PARAMETER:
            case UML_FINAL_STATE:
            case UML_PSEUDOSTATE:
                return 2;
            // level 3
            case UML_MESSAGE:
            case UML_TRANSITION:
                return 3;
            // level 4
            case UML_EVENT:
            case UML_OPAQUE_BEHAVIOR:
                return 4;
            // level 5, 6
            case UML_ASSOCIATION_END:
                return 5;
            case UML_ASSOCIATION:
                return 6;
            // ignored
            default:
                return 100;
        }
    }

    public static int compareElement(UmlElement e1, UmlElement e2) {
        return Integer.compare(elementLevel(e1), elementLevel(e2));
    }
}
