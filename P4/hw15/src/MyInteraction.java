import com.oocourse.uml3.interact.common.Pair;
import com.oocourse.uml3.interact.exceptions.user.LifelineCreatedRepeatedlyException;
import com.oocourse.uml3.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNeverCreatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml3.models.elements.UmlEndpoint;
import com.oocourse.uml3.models.elements.UmlInteraction;
import com.oocourse.uml3.models.elements.UmlMessage;

import java.util.ArrayList;
import java.util.HashMap;

public class MyInteraction {
    private int lifeLines = 0;
    private final UmlInteraction umlInteraction;
    private final HashMap<String, ArrayList<MyLifeLine>> name2lifeLine = new HashMap<>();

    public MyInteraction(UmlInteraction umlInteraction) {
        this.umlInteraction = umlInteraction;
    }

    public void addLifeline(MyLifeLine myLifeLine) {
        lifeLines++;
        if (!name2lifeLine.containsKey(myLifeLine.getName())) {
            name2lifeLine.put(myLifeLine.getName(), new ArrayList<>());
        }
        name2lifeLine.get(myLifeLine.getName()).add(myLifeLine);
    }

    // 顺序图 1
    public int getParticipantCount() {
        return lifeLines;
    }

    private MyLifeLine findLifeline(String lifelineName)
            throws LifelineNotFoundException, LifelineDuplicatedException {
        ArrayList<MyLifeLine> lifelines = name2lifeLine.get(lifelineName);
        if (lifelines == null) {
            throw new LifelineNotFoundException(getName(), lifelineName);
        } else if (lifelines.size() != 1) {
            throw new LifelineDuplicatedException(getName(), lifelineName);
        }
        return lifelines.get(0);
    }

    // 顺序图 2
    public String getParticipantCreator(String lifelineName)
            throws LifelineNotFoundException, LifelineDuplicatedException,
            LifelineNeverCreatedException, LifelineCreatedRepeatedlyException {
        MyLifeLine myLifeLine = findLifeline(lifelineName);
        ArrayList<String> res = myLifeLine.getParticipantCreator();
        if (res.size() == 0) {
            throw new LifelineNeverCreatedException(getName(), lifelineName);
        } else if (res.size() != 1) {
            throw new LifelineCreatedRepeatedlyException(getName(), lifelineName);
        }
        return res.get(0);
    }

    // 顺序图 3
    public Pair<Integer, Integer> getParticipantLostAndFound(String lifelineName,
                                                             HashMap<String, Object> id2elm)
            throws LifelineNotFoundException, LifelineDuplicatedException {
        MyLifeLine myLifeLine = findLifeline(lifelineName);
        ArrayList<UmlMessage> receiveMessages = new ArrayList<>(myLifeLine.getReceiveMessages());
        ArrayList<UmlMessage> sendMessages = new ArrayList<>(myLifeLine.getSendMessages());
        receiveMessages.removeIf(umlMessage -> !isEndPoint(umlMessage.getSource(), id2elm));
        sendMessages.removeIf(umlMessage -> !isEndPoint(umlMessage.getTarget(), id2elm));
        return new Pair<>(receiveMessages.size(), sendMessages.size());
    }

    private boolean isEndPoint(String id, HashMap<String, Object> id2elm) {
        Object object = id2elm.get(id);
        return object instanceof UmlEndpoint;
    }

    public String getName() {
        return this.umlInteraction.getName();
    }

    public String getParentId() {
        return this.umlInteraction.getParentId();
    }
}
