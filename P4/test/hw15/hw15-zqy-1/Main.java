import java.util.Arrays;
import java.util.List;
import java.util.Map;
import com.oocourse.uml3.interact.AppRunner;
import com.oocourse.uml3.interact.common.Pair;
import com.oocourse.uml3.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.LifelineCreatedRepeatedlyException;
import com.oocourse.uml3.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNeverCreatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml3.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.TransitionNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.UmlRule001Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule002Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule003Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule004Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule005Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule006Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule007Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule008Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule009Exception;
import com.oocourse.uml3.interact.format.UserApi;
import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlElement;
import com.oocourse.uml3.models.elements.UmlLifeline;

public class Main implements UserApi {
    public static void main(String[] args) throws Exception {
        AppRunner.newInstance(Main.class).run(args);
    }

    private final Unit1 unit1;
    private final Unit2A unit2a;
    private final Unit2B unit2b;

    public Main(UmlElement... elements) {
        // sort by dependency level, stable according to doc
        Arrays.sort(elements, (e1, e2) -> Util.compareElement(e1, e2));
        // construct parts
        unit1 = new Unit1(elements);
        unit2a = new Unit2A(elements);
        unit2b = new Unit2B(elements);
    }

    public int getClassCount() {
        return unit1.getClassCount();
    }

    public int getClassSubClassCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return unit1.getClassSubClassCount(className);
    }

    public int getClassOperationCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return unit1.getClassOperationCount(className);
    }

    public Map<Visibility, Integer> getClassOperationVisibility(String className, String methodName)
            throws ClassNotFoundException, ClassDuplicatedException {
        return unit1.getClassOperationVisibility(className, methodName);
    }

    public List<Integer> getClassOperationCouplingDegree(String className, String methodName)
            throws ClassNotFoundException, ClassDuplicatedException, MethodWrongTypeException,
            MethodDuplicatedException {
        return unit1.getClassOperationCouplingDegree(className, methodName);
    }

    public int getClassAttributeCouplingDegree(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return unit1.getClassAttributeCouplingDegree(className);
    }

    public List<String> getClassImplementInterfaceList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return unit1.getClassImplementInterfaceList(className);
    }

    public int getClassDepthOfInheritance(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return unit1.getClassDepthOfInheritance(className);
    }

    public int getParticipantCount(String interactionName)
            throws InteractionNotFoundException, InteractionDuplicatedException {
        return unit2a.getParticipantCount(interactionName);
    }

    public UmlLifeline getParticipantCreator(String interactionName, String lifelineName)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException, LifelineNeverCreatedException,
            LifelineCreatedRepeatedlyException {
        return unit2a.getParticipantCreator(interactionName, lifelineName);
    }

    public Pair<Integer, Integer> getParticipantLostAndFound(String interactionName,
            String lifelineName) throws InteractionNotFoundException,
            InteractionDuplicatedException, LifelineNotFoundException, LifelineDuplicatedException {
        return unit2a.getParticipantLostAndFound(interactionName, lifelineName);
    }

    public int getStateCount(String stateMachineName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException {
        return unit2b.getStateCount(stateMachineName);
    }

    public boolean getStateIsCriticalPoint(String stateMachineName, String stateName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException {
        return unit2b.getStateIsCriticalPoint(stateMachineName, stateName);
    }

    public List<String> getTransitionTrigger(String stateMachineName, String sourceStateName,
            String targetStateName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException, TransitionNotFoundException {
        return unit2b.getTransitionTrigger(stateMachineName, sourceStateName, targetStateName);
    }

    public void checkForUml001() throws UmlRule001Exception {
        unit1.checkForRule001();
    }

    public void checkForUml002() throws UmlRule002Exception {
        unit1.checkForRule002();
    }

    public void checkForUml003() throws UmlRule003Exception {
        unit1.checkForRule003();
    }

    public void checkForUml004() throws UmlRule004Exception {
        unit1.checkForRule004();
    }

    public void checkForUml005() throws UmlRule005Exception {
        unit1.checkForRule005();
    }

    public void checkForUml006() throws UmlRule006Exception {
        unit2a.checkForRule006();
    }

    public void checkForUml007() throws UmlRule007Exception {
        unit2a.checkForRule007();
    }

    public void checkForUml008() throws UmlRule008Exception {
        unit2b.checkForRule008();
    }

    public void checkForUml009() throws UmlRule009Exception {
        unit2b.checkForRule009();
    }
}
