import com.oocourse.uml3.interact.common.AttributeClassInformation;
import com.oocourse.uml3.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml3.models.common.NameableType;
import com.oocourse.uml3.models.common.ReferenceType;
import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlAttribute;
import com.oocourse.uml3.models.elements.UmlClass;
import com.oocourse.uml3.models.elements.UmlParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MyClass extends MyInterfaceClass {
    private final UmlClass umlClass;
    private final ArrayList<MyInterface> realizations = new ArrayList<>();

    public MyClass(UmlClass umlClass) {
        this.umlClass = umlClass;
        setUmlElement(umlClass);
    }

    public void addRealization(MyInterface myInterface) {
        realizations.add(myInterface);
    }

    public List<MyInterface> getRealizations() {
        return realizations;
        // return realizations.stream().map(MyInterface::getName).collect(Collectors.toList());
    }

    // 类图 2
    public int getSubClassCount() {
        return getChildren().size();
    }

    // 类图 3
    public int getOperationCount() {
        return getOperations().size();
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

    // 类图 4
    public Map<Visibility, Integer> getClassOperationVisibility(String methodName) {
        Map<Visibility, Integer> map = new HashMap<>();
        // public/protected/private/package-private
        map.put(Visibility.PUBLIC, 0);
        map.put(Visibility.PROTECTED, 0);
        map.put(Visibility.PRIVATE, 0);
        map.put(Visibility.PACKAGE, 0);
        ArrayList<MyOperation> operations = getOperations();
        for (MyOperation operation : operations) {
            if (operation.getName().equals(methodName)) {
                map.put(operation.getVisibility(), map.get(operation.getVisibility()) + 1);
            }
        }
        return map;
    }

    // 类图 5
    public List<Integer> getClassOperationCouplingDegree(String methodName, Set<String> ids)
            throws MethodWrongTypeException, MethodDuplicatedException {
        ArrayList<MyOperation> operations = new ArrayList<>(getOperations());
        operations.removeIf(myOperation -> !(myOperation.getName().equals(methodName)));

        // check for wrong type
        for (MyOperation operation : operations) {
            if (operation.hasWrongType(ids)) {
                throw new MethodWrongTypeException(getName(), methodName);
            }
        }

        // check for duplicated operation
        for (int i = 0; i < operations.size(); i++) {
            MyOperation myOperation = operations.get(i);
            for (int j = 0; j < i; j++) {
                MyOperation operation = operations.get(j);
                if (isDuplicateParas(myOperation.getInParas(), operation.getInParas())) {
                    throw new MethodDuplicatedException(getName(), methodName);
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
                            (!referenceType.getReferenceId().equals(this.getId()))) {
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

    // 类图 6
    public int getClassAttributeCouplingDegree() {
        HashSet<ReferenceType> referenceTypes = new HashSet<>();
        getTotalAttributeReferenceTypes(this, referenceTypes);
        referenceTypes.removeIf(referenceType -> referenceType.getReferenceId().equals(getId()));
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

    // 类图 7
    public List<String> getClassImplementInterfaceList() {
        // 类实现接口
        HashSet<MyInterface> res = new HashSet<>(this.getRealizations());
        MyClass myClass = this;
        while (myClass.getParents().size() != 0) {
            myClass = (MyClass) myClass.getParents().get(0);  // 遍历所有的父类
            res.addAll(myClass.getRealizations());  // 把实现的接口都保存起来
        }
        // 接口继承
        HashSet<MyInterface> newRes = new HashSet<>();
        for (MyInterface myInterface : res) {
            myInterface.getAllRelatedInterfaces(newRes);
        }
        return newRes.stream().map(MyInterface::getName).collect(Collectors.toList());
    }

    // 类图 8
    public int getClassDepthOfInheritance() {
        MyClass myClass = this;
        int depth = 0;
        while (myClass.getParents().size() != 0) {
            myClass = (MyClass) myClass.getParents().get(0);
            depth++;
        }
        return depth;
    }

    private boolean isEmptyName(String name) {
        if (name != null) {
            char[] chars = name.toCharArray();
            for (char c : chars) {
                if (c != ' ' && c != '\t') {
                    return false;
                }
            }
        }
        return true;
    }

    public HashSet<AttributeClassInformation> getSameMembers() {
        HashSet<AttributeClassInformation> information = new HashSet<>();
        HashSet<String> exists = new HashSet<>();
        for (UmlAttribute attribute : getAttributes()) {
            if (exists.contains(attribute.getName())) {
                information.add(new AttributeClassInformation(attribute.getName(), getName()));
            }
            exists.add(attribute.getName());
        }
        for (String associationEnd : getAssociationEnds()) {
            if (!isEmptyName(associationEnd)) {
                if (exists.contains(associationEnd)) {
                    information.add(new AttributeClassInformation(associationEnd, getName()));
                }
                exists.add(associationEnd);
            }
        }
        return information;
    }
}
