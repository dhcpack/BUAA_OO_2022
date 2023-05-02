import java.util.HashMap;
import com.oocourse.uml3.interact.common.Pair;
import com.oocourse.uml3.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.LifelineCreatedRepeatedlyException;
import com.oocourse.uml3.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNeverCreatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.UmlRule006Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule007Exception;
import com.oocourse.uml3.interact.format.UmlCollaborationApi;
import com.oocourse.uml3.models.common.MessageSort;
import com.oocourse.uml3.models.elements.UmlAttribute;
import com.oocourse.uml3.models.elements.UmlCollaboration;
// - import com.oocourse.uml3.models.elements.UmlAttribute;
// - import com.oocourse.uml3.models.elements.UmlCollaboration;
import com.oocourse.uml3.models.elements.UmlElement;
import com.oocourse.uml3.models.elements.UmlEndpoint;
import com.oocourse.uml3.models.elements.UmlInteraction;
import com.oocourse.uml3.models.elements.UmlLifeline;
import com.oocourse.uml3.models.elements.UmlMessage;

public class Unit2A implements UmlCollaborationApi {
    private final HashMap<String, MyCollab> collabs = new HashMap<>();
    private final HashMap<String, UmlAttribute> uattrs = new HashMap<>();
    private final HashMap<String, MyInter> inters = new HashMap<>();
    private final HashMap<String, MyEP> eps = new HashMap<>();
    private final HashMap<String, MyLL> lls = new HashMap<>();
    private final HashMap<String, MyLLep> lleps = new HashMap<>();
    private final HashMap<String, UmlMessage> umsgs = new HashMap<>();

    public Unit2A(UmlElement[] elements) {
        for (UmlElement e : elements) {
            String eid = e.getId();
            switch (e.getElementType()) {
                // level 0
                case UML_COLLABORATION:
                    collabs.put(eid, new MyCollab((UmlCollaboration) e));
                    break;
                // level 1
                case UML_ATTRIBUTE:
                    // is of collab, not inter or class
                    if (collabs.containsKey(e.getParentId())) {
                        collabs.get(e.getParentId()).getAttrs().add(eid);
                    }
                    uattrs.put(eid, (UmlAttribute) e); // add
                    break;
                case UML_INTERACTION:
                    // - collabs.get(e.getParentId()).getInters().add(eid);
                    inters.put(eid, new MyInter((UmlInteraction) e)); // add
                    break;
                // level 2
                case UML_ENDPOINT:
                    inters.get(e.getParentId()).getEndpoints().add(eid);
                    MyEP ep = new MyEP((UmlEndpoint) e);
                    eps.put(eid, ep);
                    lleps.put(eid, ep); // add
                    break;
                case UML_LIFELINE:
                    inters.get(e.getParentId()).getLifelines().add(eid);
                    MyLL ll = new MyLL((UmlLifeline) e);
                    lls.put(eid, ll);
                    lleps.put(eid, ll); // add
                    break;
                // level 3
                case UML_MESSAGE:
                    inters.get(e.getParentId()).getMsgs().add(eid);
                    UmlMessage umsg = (UmlMessage) e;
                    lleps.get(umsg.getSource()).getMsgSent().add(eid);
                    MyLLep recver = lleps.get(umsg.getTarget());
                    recver.getMsgRecv().add(eid);
                    recver.recvMsg(umsg.getMessageSort() == MessageSort.DELETE_MESSAGE);
                    umsgs.put(eid, umsg); // add
                    break;
                // others
                default:
                    break;
            }
        }
    }

    public void checkForRule006() throws UmlRule006Exception {
        for (MyLL ll : lls.values()) {
            UmlLifeline ull = ll.getUll(); // LL -> Inter -> Collab
            String llpp = inters.get(ull.getParentId()).getUmlInter().getParentId();
            UmlAttribute uattr = uattrs.get(ull.getRepresent()); // Attr -> Collab
            String attrp = uattr.getParentId();
            if (!llpp.equals(attrp)) {
                // not same parent
                throw new UmlRule006Exception();
            }
        }
    }

    public void checkForRule007() throws UmlRule007Exception {
        for (MyLL ll : lls.values()) {
            if (ll.isViolatedRule007()) {
                throw new UmlRule007Exception();
            }
        }
    }

    private MyInter findInter(String interactionName)
            throws InteractionNotFoundException, InteractionDuplicatedException {
        MyInter result = null;
        for (MyInter obj : inters.values()) {
            if (interactionName.equals(obj.getUmlInter().getName())) {
                if (result != null) {
                    throw new InteractionDuplicatedException(interactionName);
                }
                result = obj;
            }
        }
        if (result == null) {
            throw new InteractionNotFoundException(interactionName);
        }
        return result;
    }

    public int getParticipantCount(String interactionName)
            throws InteractionNotFoundException, InteractionDuplicatedException {
        return findInter(interactionName).getLifelines().size();
    }

    private Pair<MyInter, MyLL> findLifeline(String interactionName, String lifelineName)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        MyInter inter = findInter(interactionName);
        MyLL result = null;
        for (String llid : inter.getLifelines()) {
            MyLL ll = lls.get(llid);
            if (lifelineName.equals(ll.getUll().getName())) {
                if (result != null) {
                    throw new LifelineDuplicatedException(interactionName, lifelineName);
                }
                result = ll;
            }
        }
        if (result == null) {
            throw new LifelineNotFoundException(interactionName, lifelineName);
        }
        return new Pair<>(inter, result);
    }

    public UmlLifeline getParticipantCreator(String interactionName, String lifelineName)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException, LifelineNeverCreatedException,
            LifelineCreatedRepeatedlyException {
        Pair<MyInter, MyLL> pair = findLifeline(interactionName, lifelineName);
        MyLL ll = pair.getSecond();
        UmlMessage result = null;
        for (String msgId : ll.getMsgRecv()) {
            UmlMessage umsg = umsgs.get(msgId);
            if (umsg.getMessageSort() == MessageSort.CREATE_MESSAGE) {
                if (result != null) {
                    throw new LifelineCreatedRepeatedlyException(interactionName, lifelineName);
                }
                result = umsg;
            }
        }
        if (result == null) {
            throw new LifelineNeverCreatedException(interactionName, lifelineName);
        }
        // assume sender is a lifeline
        return lls.get(result.getSource()).getUll();
    }

    public Pair<Integer, Integer> getParticipantLostAndFound(String interactionName,
            String lifelineName) throws InteractionNotFoundException,
            InteractionDuplicatedException, LifelineNotFoundException, LifelineDuplicatedException {
        Pair<MyInter, MyLL> pair = findLifeline(interactionName, lifelineName);
        MyLL ll = pair.getSecond();

        int founds = 0;
        for (String msgId : ll.getMsgRecv()) {
            // found: from Endpoint
            UmlMessage umsg = umsgs.get(msgId);
            if (lleps.get(umsg.getSource()) instanceof MyEP) {
                founds++;
            }
        }
        int losts = 0;
        for (String msgId : ll.getMsgSent()) {
            // lost: to Endpoint
            UmlMessage umsg = umsgs.get(msgId);
            if (lleps.get(umsg.getTarget()) instanceof MyEP) {
                losts++;
            }
        }
        return new Pair<>(founds, losts);
    }
}
