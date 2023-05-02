import com.oocourse.uml1.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml1.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml1.interact.format.UserApi;
import com.oocourse.uml1.models.common.Direction;
import com.oocourse.uml1.models.common.Visibility;
import com.oocourse.uml1.models.elements.UmlAttribute;
import com.oocourse.uml1.models.elements.UmlClass;
import com.oocourse.uml1.models.elements.UmlElement;
import com.oocourse.uml1.models.elements.UmlGeneralization;
import com.oocourse.uml1.models.elements.UmlInterface;
import com.oocourse.uml1.models.elements.UmlInterfaceRealization;
import com.oocourse.uml1.models.elements.UmlOperation;
import com.oocourse.uml1.models.elements.UmlParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyImplementation implements UserApi {
    // for all
    private final HashMap<String, Object> id2elm = new HashMap<>();
    // 类图
    private final HashMap<String, ArrayList<MyClass>> name2class = new HashMap<>();
    private int classCount = 0;

    public MyImplementation(UmlElement... elements) {
        level1(elements);
        level2(elements);
        level3(elements);
    }

    // interface, class, interaction, statemachine
    private void level1(UmlElement... elements) {
        for (UmlElement e : elements) {
            switch (e.getElementType()) {
                case UML_INTERFACE:
                    id2elm.put(e.getId(), new MyInterface((UmlInterface) e));
                    break;
                case UML_CLASS:
                    classCount++;
                    MyClass myClass = new MyClass((UmlClass) e);
                    id2elm.put(e.getId(), myClass);
                    if (!name2class.containsKey(e.getName())) {
                        name2class.put(e.getName(), new ArrayList<>());
                    }
                    name2class.get(e.getName()).add(myClass);
                    break;
                default:
                    break;
            }
        }
    }

    // interface_realization, generalization, attribute, operation, region
    private void level2(UmlElement... elements) {
        for (UmlElement e : elements) {
            switch (e.getElementType()) {
                case UML_INTERFACE_REALIZATION:
                    UmlInterfaceRealization realization = (UmlInterfaceRealization) e;
                    MyInterface myInterface = (MyInterface) id2elm.get(realization.getTarget());
                    MyClass myClass = (MyClass) id2elm.get(realization.getSource());
                    myClass.addRealization(myInterface);
                    break;
                case UML_GENERALIZATION:
                    UmlGeneralization generalization = (UmlGeneralization) e;
                    MyInterfaceClass icSource = (MyInterfaceClass) id2elm.get(
                            generalization.getSource());
                    MyInterfaceClass icTarget = (MyInterfaceClass) id2elm.get(
                            generalization.getTarget());
                    icSource.addParent(icTarget);
                    icTarget.addChild(icSource);
                    break;
                case UML_ATTRIBUTE:
                    UmlAttribute attribute = (UmlAttribute) e;
                    MyInterfaceClass ic = (MyInterfaceClass) id2elm.get(e.getParentId());
                    if (ic != null) {  // 对parent是Collaboration的attribute不做处理
                        ic.addAttribute(attribute);
                    }
                    break;
                case UML_OPERATION:
                    MyOperation operation = new MyOperation((UmlOperation) e);
                    id2elm.put(e.getId(), operation);
                    myClass = (MyClass) id2elm.get(e.getParentId());
                    myClass.addOperation(operation);
                    break;
                default:
                    break;
            }
        }
    }

    // parameter, lifeline, endPoint, state, FinalState, PseudoState  状态信息(图中的点)
    private void level3(UmlElement... elements) {
        for (UmlElement e : elements) {
            switch (e.getElementType()) {
                case UML_PARAMETER:
                    UmlParameter parameter = (UmlParameter) e;
                    MyOperation operation = (MyOperation) id2elm.get(e.getParentId());
                    if (parameter.getDirection() == Direction.IN) {
                        operation.addInParas(parameter);
                    } else {
                        operation.addReturnParas(parameter);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public MyClass findClass(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        ArrayList<MyClass> classes = name2class.get(className);
        if (classes == null) {
            throw new ClassNotFoundException(className);
        } else if (classes.size() != 1) {
            throw new ClassDuplicatedException(className);
        }
        return classes.get(0);
    }

    // 1. CLASS_COUNT
    public int getClassCount() {
        return classCount;
    }

    // 2. CLASS_SUBCLASS_COUNT
    public int getClassSubClassCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return findClass(className).getSubClassCount();
    }

    // 3. CLASS_OPERATION_COUNT
    public int getClassOperationCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return findClass(className).getOperationCount();
    }

    // 4. CLASS_OPERATION_VISIBILITY
    public Map<Visibility, Integer> getClassOperationVisibility(String className, String methodName)
            throws ClassNotFoundException, ClassDuplicatedException {
        return findClass(className).getClassOperationVisibility(methodName);
    }

    // 5. CLASS_OPERATION_COUPLING_DEGREE
    public List<Integer> getClassOperationCouplingDegree(String className, String methodName)
            throws ClassNotFoundException, ClassDuplicatedException, MethodWrongTypeException,
            MethodDuplicatedException {
        return findClass(className).getClassOperationCouplingDegree(methodName, id2elm.keySet());
    }

    // 6. CLASS_ATTR_COUPLING_DEGREE
    public int getClassAttributeCouplingDegree(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return findClass(className).getClassAttributeCouplingDegree();
    }

    // 7. CLASS_IMPLEMENT_INTERFACE_LIST
    public List<String> getClassImplementInterfaceList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return findClass(className).getClassImplementInterfaceList();
    }

    // 8. CLASS_DEPTH_OF_INHERITANCE
    public int getClassDepthOfInheritance(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return findClass(className).getClassDepthOfInheritance();
    }
}