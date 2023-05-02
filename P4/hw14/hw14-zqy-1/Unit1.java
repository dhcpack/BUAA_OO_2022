import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.oocourse.uml2.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml2.interact.format.UmlClassModelApi;
import com.oocourse.uml2.models.common.Direction;
import com.oocourse.uml2.models.common.NameableType;
import com.oocourse.uml2.models.common.NamedType;
import com.oocourse.uml2.models.common.ReferenceType;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlAttribute;
import com.oocourse.uml2.models.elements.UmlClass;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlGeneralization;
import com.oocourse.uml2.models.elements.UmlInterface;
import com.oocourse.uml2.models.elements.UmlInterfaceRealization;
import com.oocourse.uml2.models.elements.UmlOperation;
import com.oocourse.uml2.models.elements.UmlParameter;

public class Unit1 implements UmlClassModelApi {
    private final HashMap<String, MyInterCls> interCls = new HashMap<>();
    private final HashMap<String, MyClass> classes = new HashMap<>();
    private final HashMap<String, MyIntf> intfs = new HashMap<>();
    private final HashMap<String, UmlAttribute> uattrs = new HashMap<>();
    private final HashMap<String, MyOp> ops = new HashMap<>();
    private final HashMap<String, UmlParameter> uparas = new HashMap<>();

    public Unit1(UmlElement[] elements) {
        // sorted by Main
        // read all elements
        for (UmlElement e : elements) {
            String eid = e.getId();
            switch (e.getElementType()) {
                // level 0
                case UML_CLASS: {
                    MyClass cls = new MyClass((UmlClass) e);
                    classes.put(eid, cls);
                    interCls.put(eid, cls); // add
                    break;
                }
                case UML_INTERFACE: {
                    MyIntf intf = new MyIntf((UmlInterface) e);
                    intfs.put(eid, intf);
                    interCls.put(eid, intf); // add
                    break;
                }
                // level 1
                case UML_ATTRIBUTE: {
                    if (interCls.containsKey(e.getParentId())) {
                        // of inter or class, not collaboration
                        interCls.get(e.getParentId()).getAttrs().add(eid);
                        uattrs.put(eid, (UmlAttribute) e); // add
                    }
                    break;
                }
                case UML_OPERATION: {
                    classes.get(e.getParentId()).getOps().add(eid);
                    ops.put(eid, new MyOp((UmlOperation) e)); // add
                    break;
                }
                case UML_GENERALIZATION: {
                    UmlGeneralization ugen = (UmlGeneralization) e;
                    String genSrc = ugen.getSource();
                    String genTgt = ugen.getTarget();
                    interCls.get(genSrc).getGenTgts().add(genTgt);
                    interCls.get(genTgt).getGenSrcs().add(genSrc);
                    break;
                }
                case UML_INTERFACE_REALIZATION: {
                    UmlInterfaceRealization uir = (UmlInterfaceRealization) e;
                    String realSrc = uir.getSource(); // class
                    String realTgt = uir.getTarget(); // interface
                    classes.get(realSrc).getRealTgts().add(realTgt);
                    intfs.get(realTgt).getRealSrcs().add(realSrc);
                    break;
                }
                // level 2
                case UML_PARAMETER: {
                    ops.get(e.getParentId()).getParas().add(eid);
                    uparas.put(eid, (UmlParameter) e); // add
                    break;
                }
                // ignored
                case UML_ASSOCIATION:
                case UML_ASSOCIATION_END:
                default:
                    break;
            }
        }
    }

    public int getClassCount() {
        return classes.size();
    }

    private MyClass findClassByName(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass result = null;
        for (MyClass cls : classes.values()) {
            if (className.equals(cls.getUmlClass().getName())) {
                if (result != null) {
                    // already found
                    throw new ClassDuplicatedException(className);
                }
                result = cls;
            }
        }
        if (result == null) {
            // not found
            throw new ClassNotFoundException(className);
        }
        return result;
    }

    public int getClassSubClassCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        // direct sub class count
        return findClassByName(className).getGenSrcs().size();
    }

    public int getClassOperationCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        // own operation count
        return findClassByName(className).getOps().size();
    }

    public Map<Visibility, Integer> getClassOperationVisibility(String className, String methodName)
            throws ClassNotFoundException, ClassDuplicatedException {
        // own operation visibilities
        HashMap<Visibility, Integer> result = new HashMap<>();
        findClassByName(className).getOps().stream().map(id -> ops.get(id).getUmlOp())
                .forEach(uop -> {
                    if (methodName.equals(uop.getName())) {
                        Visibility vis = uop.getVisibility();
                        result.put(vis, 1 + result.getOrDefault(vis, 0));
                    }
                });
        return result;
    }

    private boolean isWrongType(NameableType nbt, boolean isReturnType) {
        if (nbt instanceof NamedType) {
            NamedType nt = (NamedType) nbt;
            switch (nt.getName()) {
                case "byte":
                case "short":
                case "int":
                case "long":
                case "float":
                case "double":
                case "char":
                case "boolean":
                case "String":
                    return false;
                case "void":
                    return !isReturnType;
                default:
                    return true;
            }
        }
        if (nbt instanceof ReferenceType) {
            ReferenceType rt = (ReferenceType) nbt;
            return !interCls.containsKey(rt.getReferenceId());
        }
        return true;
    }

    private boolean isMyOpEqual(MyOp op1, MyOp op2) {
        List<NameableType> op1types = op1.getParas().stream() //
                .map(id -> uparas.get(id)) // id to UmlParameter
                .filter(upara -> upara.getDirection() == Direction.IN) // only INs
                .map(upara -> upara.getType()).collect(Collectors.toList());
        List<NameableType> op2types = op2.getParas().stream() //
                .map(id -> uparas.get(id)) // id to UmlParameter
                .filter(upara -> upara.getDirection() == Direction.IN) // only INs
                .map(upara -> upara.getType()).collect(Collectors.toList());
        if (op1types.size() != op2types.size()) {
            return false;
        }
        for (NameableType nbt : op1types) {
            // used Object.equals()
            if (!op2types.remove(nbt)) {
                return false;
            }
        }
        return true;
    }

    public List<Integer> getClassOperationCouplingDegree(String className, String methodName)
            throws ClassNotFoundException, ClassDuplicatedException, MethodWrongTypeException,
            MethodDuplicatedException {
        MyClass queryCls = findClassByName(className);
        // coupling degree of own operations
        List<MyOp> opList = queryCls.getOps().stream() //
                .map(opId -> ops.get(opId)) // id to MyOp
                .filter(op -> methodName.equals(op.getUmlOp().getName())) // with name
                .collect(Collectors.toList()); // collect
        // check wrong type error
        for (MyOp op : opList) {
            List<UmlParameter> uparaList =
                    op.getParas().stream().map(id -> uparas.get(id)).collect(Collectors.toList());
            for (UmlParameter upara : uparaList) {
                if (isWrongType(upara.getType(), upara.getDirection() == Direction.RETURN)) {
                    throw new MethodWrongTypeException(className, methodName);
                }
            }
        }
        // check duplicate method error
        int n = opList.size();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (isMyOpEqual(opList.get(i), opList.get(j))) {
                    throw new MethodDuplicatedException(className, methodName);
                }
            }
        }
        // calculate coupling degree
        String queryCid = queryCls.getUmlClass().getId();
        return opList.stream().map(op -> {
            HashSet<String> allRefIds = new HashSet<>();
            op.getParas().stream().map(id -> uparas.get(id).getType()).forEach(nbt -> {
                if (nbt instanceof ReferenceType) {
                    ReferenceType rt = (ReferenceType) nbt;
                    allRefIds.add(rt.getReferenceId());
                }
            });
            allRefIds.remove(queryCid);
            return allRefIds.size();
        }).collect(Collectors.toList());
    }

    public int getClassAttributeCouplingDegree(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        // of own AND all super classes, BUT without interfaces.
        HashSet<String> allRefIds = new HashSet<>();
        MyClass currCls = findClassByName(className);
        String queryCid = currCls.getUmlClass().getId();
        while (true) {
            currCls.getAttrs().stream().map(aId -> uattrs.get(aId).getType()).forEach(nbt -> {
                if (nbt instanceof ReferenceType) {
                    ReferenceType rt = (ReferenceType) nbt;
                    allRefIds.add(rt.getReferenceId());
                }
            });
            HashSet<String> superIds = currCls.getGenTgts();
            if (superIds.size() == 0) {
                break;
            }
            // assume class single inheritance
            currCls = classes.get(superIds.toArray()[0]);
        }
        allRefIds.remove(queryCid);
        return allRefIds.size();
    }

    public List<String> getClassImplementInterfaceList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        HashSet<String> visited = new HashSet<>();
        LinkedList<String> queue = new LinkedList<>();
        queue.add(findClassByName(className).getUmlClass().getId());
        while (!queue.isEmpty()) {
            String icId = queue.removeFirst();
            if (!visited.add(icId)) {
                continue;
            }
            MyInterCls ic = interCls.get(icId);
            // visit all super classes (or interfaces for interfaces)
            queue.addAll(ic.getGenTgts());
            if (ic instanceof MyClass) {
                // is class, visit its interfaces
                queue.addAll(((MyClass) ic).getRealTgts());
            }
        }
        return new ArrayList<String>(visited.stream() //
                .map(icId -> interCls.get(icId)) // map id to InterCls
                .filter(ic -> ic instanceof MyIntf) // get all Interfaces
                .map(intf -> ((MyIntf) intf).getUmlIntf().getName()) // to name
                .collect(Collectors.toSet()) // collect to set
        ); // to array
    }

    public int getClassDepthOfInheritance(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        int depth = 0;
        MyClass curr = findClassByName(className);
        while (true) {
            HashSet<String> superIds = curr.getGenTgts();
            if (superIds.size() == 0) {
                break;
            }
            // assume class single inheritance
            curr = classes.get(superIds.toArray()[0]);
            depth++;
        }
        return depth;
    }
}
