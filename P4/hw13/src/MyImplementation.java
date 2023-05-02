import com.oocourse.uml1.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml1.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml1.interact.format.UserApi;
import com.oocourse.uml1.models.common.Direction;
import com.oocourse.uml1.models.common.NameableType;
import com.oocourse.uml1.models.common.ReferenceType;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MyImplementation implements UserApi {
    // key = name, value = classes of this name
    private final HashMap<String, ArrayList<MyClass>> name2class = new HashMap<>();
    // key = id, value = UmlElement
    private final HashMap<String, Object> id2elm = new HashMap<>();
    private int classCount = 0;

    public MyImplementation(UmlElement... elements) {
        level1(elements);
        level2(elements);
        level3(elements);
    }

    // interface, class, association_end
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
                case UML_ASSOCIATION_END:
                default:
                    break;
            }
        }
    }

    // interface_realization, generalization, attribute, operation, association
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
                    ic.addAttribute(attribute);
                    break;
                case UML_OPERATION:
                    MyOperation operation = new MyOperation((UmlOperation) e);
                    id2elm.put(e.getId(), operation);
                    myClass = (MyClass) id2elm.get(e.getParentId());
                    myClass.addOperation(operation);
                    break;
                case UML_ASSOCIATION:
                default:
                    break;
            }
        }
    }

    // parameter
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
        Map<Visibility, Integer> map = new HashMap<>();
        // public/protected/private/package- private
        map.put(Visibility.PUBLIC, 0);
        map.put(Visibility.PROTECTED, 0);
        map.put(Visibility.PRIVATE, 0);
        map.put(Visibility.PACKAGE, 0);
        ArrayList<MyOperation> operations = findClass(className).getOperations();
        for (MyOperation operation : operations) {
            if (operation.getName().equals(methodName)) {
                map.put(operation.getVisibility(), map.get(operation.getVisibility()) + 1);
            }
        }
        return map;
    }

    // 5. CLASS_OPERATION_COUPLING_DEGREE
    public List<Integer> getClassOperationCouplingDegree(String className, String methodName)
            throws ClassNotFoundException, ClassDuplicatedException, MethodWrongTypeException,
            MethodDuplicatedException {
        MyClass myClass = findClass(className);
        ArrayList<MyOperation> operations = new ArrayList<>(myClass.getOperations());
        operations.removeIf(new Predicate<MyOperation>() {
            @Override
            public boolean test(MyOperation myOperation) {
                return !(myOperation.getName().equals(methodName));
            }
        });

        // check for wrong type
        for (MyOperation operation : operations) {
            if (operation.hasWrongType(id2elm.keySet())) {
                throw new MethodWrongTypeException(className, methodName);
            }
        }

        // check for duplicated operation
        for (int i = 0; i < operations.size(); i++) {
            MyOperation myOperation = operations.get(i);
            for (int j = 0; j < i; j++) {
                MyOperation operation = operations.get(j);
                if (isDuplicateParas(myOperation.getInParas(), operation.getInParas())) {
                    throw new MethodDuplicatedException(className, methodName);
                }
            }
        }

        ArrayList<Integer> res = new ArrayList<>();
        for (MyOperation operation : operations) {
            HashSet<ReferenceType> referenceTypes = new HashSet<>();
            int degree = 0;
            for (UmlParameter parameter : operation.getAllParas()) {  // 包括参数和返回值
                if (parameter.getType() instanceof ReferenceType) {
                    ReferenceType referenceType = (ReferenceType) parameter.getType();
                    if ((!referenceTypes.contains(referenceType)) &&
                            (!referenceType.getReferenceId().equals(myClass.getId()))) {
                        degree++;
                        referenceTypes.add(referenceType);
                    }
                }
            }
            res.add(degree);
        }
        return res;
    }

    private boolean isDuplicateParas(ArrayList<UmlParameter> parameters1,
                                     ArrayList<UmlParameter> parameters2) {
        if (parameters1.size() != parameters2.size()) {
            return false;
        }
        List<NameableType> type1 = parameters1.stream().map(UmlParameter::getType).
                collect(Collectors.toList());
        List<NameableType> type2 = parameters2.stream().map(UmlParameter::getType).
                collect(Collectors.toList());
        for (NameableType type : type1) {
            if (!type2.remove(type)) {
                return false;
            }
        }
        return true;
    }

    // 6. CLASS_ATTR_COUPLING_DEGREE
    public int getClassAttributeCouplingDegree(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass myClass = findClass(className);
        HashSet<ReferenceType> referenceTypes = new HashSet<>();
        getTotalAttributeReferenceTypes(myClass, referenceTypes);
        referenceTypes.removeIf(new Predicate<ReferenceType>() {
            @Override
            public boolean test(ReferenceType referenceType) {
                return referenceType.getReferenceId().equals(myClass.getId());
            }
        });
        return referenceTypes.size();
    }

    private void getTotalAttributeReferenceTypes(MyClass myClass,
                                                 HashSet<ReferenceType> referenceTypes) {
        ArrayList<UmlAttribute> attributes = myClass.getAttributes();
        for (UmlAttribute umlAttribute : attributes) {
            if (umlAttribute.getType() instanceof ReferenceType) {
                ReferenceType referenceType = (ReferenceType) umlAttribute.getType();
                referenceTypes.add(referenceType);
            }
        }
        for (MyInterfaceClass interfaceClass : myClass.getParents()) {
            if (interfaceClass instanceof MyClass) {
                getTotalAttributeReferenceTypes((MyClass) interfaceClass, referenceTypes);
            }
        }
    }

    // 7. CLASS_IMPLEMENT_INTERFACE_LIST
    public List<String> getClassImplementInterfaceList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        // 类实现接口
        MyClass myClass = findClass(className);
        HashSet<MyInterface> res = new HashSet<>(myClass.getRealizations());
        while (myClass.getParents().size() != 0) {
            myClass = (MyClass) myClass.getParents().get(0);
            res.addAll(myClass.getRealizations());
        }
        // 接口继承
        HashSet<MyInterface> newRes = new HashSet<>();
        for (MyInterface myInterface : res) {
            myInterface.getAllRelatedInterfaces(newRes);
        }
        return newRes.stream().map(MyInterface::getName).collect(Collectors.toList());
    }

    // 8. CLASS_DEPTH_OF_INHERITANCE
    public int getClassDepthOfInheritance(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass myClass = findClass(className);
        int depth = 0;
        while (myClass.getParents().size() != 0) {
            myClass = (MyClass) myClass.getParents().get(0);
            depth++;
        }
        return depth;
    }
}