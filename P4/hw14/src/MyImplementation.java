import com.oocourse.uml2.interact.common.Pair;
import com.oocourse.uml2.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.LifelineCreatedRepeatedlyException;
import com.oocourse.uml2.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNeverCreatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.TransitionNotFoundException;
import com.oocourse.uml2.interact.format.UserApi;
import com.oocourse.uml2.models.common.Direction;
import com.oocourse.uml2.models.common.ElementType;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlAttribute;
import com.oocourse.uml2.models.elements.UmlClass;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlGeneralization;
import com.oocourse.uml2.models.elements.UmlInteraction;
import com.oocourse.uml2.models.elements.UmlInterface;
import com.oocourse.uml2.models.elements.UmlInterfaceRealization;
import com.oocourse.uml2.models.elements.UmlLifeline;
import com.oocourse.uml2.models.elements.UmlMessage;
import com.oocourse.uml2.models.elements.UmlOperation;
import com.oocourse.uml2.models.elements.UmlParameter;
import com.oocourse.uml2.models.elements.UmlRegion;
import com.oocourse.uml2.models.elements.UmlEvent;
import com.oocourse.uml2.models.elements.UmlStateMachine;
import com.oocourse.uml2.models.elements.UmlTransition;

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
    // 顺序图
    private final HashMap<String, ArrayList<MyInteraction>> name2interaction = new HashMap<>();
    // 状态图
    private final HashMap<String, ArrayList<MyStateMachine>> name2statemachine = new HashMap<>();

    public MyImplementation(UmlElement... elements) {
        level1(elements);
        level2(elements);
        level3(elements);
        level4(elements);
        level5(elements);
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
                case UML_INTERACTION:
                    MyInteraction myInteraction = new MyInteraction((UmlInteraction) e);
                    id2elm.put(e.getId(), myInteraction);
                    if (!name2interaction.containsKey(e.getName())) {
                        name2interaction.put(e.getName(), new ArrayList<>());
                    }
                    name2interaction.get(e.getName()).add(myInteraction);
                    break;
                case UML_STATE_MACHINE:
                    MyStateMachine myStateMachine = new MyStateMachine((UmlStateMachine) e);
                    id2elm.put(e.getId(), myStateMachine);
                    if (!name2statemachine.containsKey(e.getName())) {
                        name2statemachine.put(e.getName(), new ArrayList<>());
                    }
                    name2statemachine.get(e.getName()).add(myStateMachine);
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
                case UML_REGION:
                    MyRegion myRegion = new MyRegion((UmlRegion) e);
                    id2elm.put(e.getId(), myRegion);
                    MyStateMachine myStateMachine = (MyStateMachine) id2elm.get(e.getParentId());
                    myStateMachine.setRegion(myRegion);
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
                case UML_LIFELINE:
                    MyLifeLine myLifeLine = new MyLifeLine((UmlLifeline) e);
                    id2elm.put(e.getId(), myLifeLine);
                    MyInteraction myInteraction = (MyInteraction) id2elm.get(e.getParentId());
                    myInteraction.addLifeline(myLifeLine);
                    break;
                case UML_ENDPOINT:
                    id2elm.put(e.getId(), e);
                    break;
                case UML_STATE:
                case UML_FINAL_STATE:
                case UML_PSEUDOSTATE:
                    MyState myState = new MyState(e);
                    id2elm.put(e.getId(), myState);
                    MyRegion myRegion = (MyRegion) id2elm.get(e.getParentId());
                    myRegion.addState(myState);
                    break;
                default:
                    break;
            }
        }
    }

    // message and transition  (图中的通路)
    private void level4(UmlElement... elements) {
        for (UmlElement e : elements) {
            switch (e.getElementType()) {
                case UML_MESSAGE:
                    UmlMessage umlMessage = (UmlMessage) e;
                    Object messageSource = id2elm.get(umlMessage.getSource());
                    if (messageSource instanceof MyLifeLine) {
                        MyLifeLine myLifeLine = (MyLifeLine) messageSource;
                        myLifeLine.addSendMessage(umlMessage);
                    }
                    Object messageTarget = id2elm.get(umlMessage.getTarget());
                    if (messageTarget instanceof MyLifeLine) {
                        MyLifeLine myLifeLine = (MyLifeLine) messageTarget;
                        myLifeLine.addReceiveMessage(umlMessage);
                    }
                    break;
                case UML_TRANSITION:
                    MyState source = (MyState) id2elm.get(((UmlTransition) e).getSource());
                    MyState target = (MyState) id2elm.get(((UmlTransition) e).getTarget());
                    MyTransition myTransition = new MyTransition((UmlTransition) e, source, target);
                    id2elm.put(e.getId(), myTransition);
                    source.addTransition(myTransition);
                    break;
                default:
                    break;
            }
        }
    }

    // event  (OPAQUE_BEHAVIOR not used)
    private void level5(UmlElement... elements) {
        for (UmlElement e : elements) {
            if (e.getElementType() == ElementType.UML_EVENT) {
                UmlEvent umlEvent = (UmlEvent) e;
                MyTransition myTransition = (MyTransition) id2elm.get(e.getParentId());
                myTransition.addEvent(umlEvent);
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

    private MyRegion findRegion(String stateMachineName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException {
        ArrayList<MyStateMachine> stateMachines = name2statemachine.get(stateMachineName);
        if (stateMachines == null) {
            throw new StateMachineNotFoundException(stateMachineName);
        } else if (stateMachines.size() != 1) {
            throw new StateMachineDuplicatedException(stateMachineName);
        }
        return stateMachines.get(0).getRegion();
    }

    // 1. STATE_COUNT
    @Override
    public int getStateCount(String stateMachineName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException {
        return findRegion(stateMachineName).getStateCount();
    }

    // 2. STATE_IS_CRITICAL_POINT
    public boolean getStateIsCriticalPoint(String stateMachineName, String stateName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException {
        return findRegion(stateMachineName).getStateIsCriticalPoint(stateMachineName, stateName);
    }

    // 3. TRANSITION_TRIGGER
    public List<String> getTransitionTrigger(String stateMachineName, String sourceStateName,
                                             String targetStateName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException, TransitionNotFoundException {
        return findRegion(stateMachineName).getTransitionTrigger(stateMachineName, sourceStateName,
                targetStateName);
    }

    private MyInteraction findInteraction(String interactionName)
            throws InteractionNotFoundException, InteractionDuplicatedException {
        ArrayList<MyInteraction> myInteraction = name2interaction.get(interactionName);
        if (myInteraction == null) {
            throw new InteractionNotFoundException(interactionName);
        } else if (myInteraction.size() != 1) {
            throw new InteractionDuplicatedException(interactionName);
        }
        return myInteraction.get(0);
    }

    // 1. PTCP_OBJ_COUNT
    public int getParticipantCount(String interactionName)
            throws InteractionNotFoundException, InteractionDuplicatedException {
        return findInteraction(interactionName).getParticipantCount();
    }

    // 2. PTCP_CREATOR
    public UmlLifeline getParticipantCreator(String interactionName, String lifelineName)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException, LifelineNeverCreatedException
            , LifelineCreatedRepeatedlyException {
        return ((MyLifeLine) id2elm.get(findInteraction(interactionName)
                .getParticipantCreator(lifelineName))).getUmlLifeline();
    }

    // 3. PTCP_LOST_AND_FOUND
    public Pair<Integer, Integer> getParticipantLostAndFound(String interactionName,
                                                             String lifelineName)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        return findInteraction(interactionName).getParticipantLostAndFound(lifelineName, id2elm);
    }
}