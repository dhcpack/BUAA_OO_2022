import com.oocourse.uml3.models.common.MessageSort;
import com.oocourse.uml3.models.elements.UmlLifeline;
import com.oocourse.uml3.models.elements.UmlMessage;

import java.util.ArrayList;

public class MyLifeLine {
    private final UmlLifeline umlLifeline;
    private final ArrayList<UmlMessage> sendMessages = new ArrayList<>();
    private final ArrayList<UmlMessage> receiveMessages = new ArrayList<>();

    public MyLifeLine(UmlLifeline umlLifeline) {
        this.umlLifeline = umlLifeline;
    }

    public void addSendMessage(UmlMessage umlMessage) {
        this.sendMessages.add(umlMessage);
    }

    public void addReceiveMessage(UmlMessage umlMessage) {
        this.receiveMessages.add(umlMessage);
    }

    public ArrayList<String> getParticipantCreator() {
        ArrayList<String> res = new ArrayList<>();
        for (UmlMessage message : receiveMessages) {
            if (message.getMessageSort() == MessageSort.CREATE_MESSAGE) {
                res.add(message.getSource());
            }
        }
        return res;
    }

    public ArrayList<UmlMessage> getSendMessages() {
        return this.sendMessages;
    }

    public ArrayList<UmlMessage> getReceiveMessages() {
        return this.receiveMessages;
    }

    public String getName() {
        return this.umlLifeline.getName();
    }

    public UmlLifeline getUmlLifeline() {
        return this.umlLifeline;
    }

    public String getRepresent() {
        return this.umlLifeline.getRepresent();
    }

    public String getParentId() {
        return this.umlLifeline.getParentId();
    }

    public boolean checkMessage() {
        for (int i = 0; i < receiveMessages.size(); i++) {
            UmlMessage umlMessage = receiveMessages.get(i);
            if (umlMessage.getMessageSort() == MessageSort.DELETE_MESSAGE &&
                    (i != receiveMessages.size() - 1)) {
                return false;
            }
        }
        return true;
    }
}
