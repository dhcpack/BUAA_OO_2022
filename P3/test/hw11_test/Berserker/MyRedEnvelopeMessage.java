import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.RedEnvelopeMessage;

public class MyRedEnvelopeMessage extends MyMessage implements RedEnvelopeMessage {

    public MyRedEnvelopeMessage(int messageId, int luckyMoney,
                                Person messagePerson1, Person messagePerson2) {
        super(messageId, luckyMoney * 5, messagePerson1, messagePerson2);
    }

    public MyRedEnvelopeMessage(int messageId, int luckMoney,
                                Person messagePerson1, Group messageGroup) {
        super(messageId, luckMoney * 5, messagePerson1, messageGroup);
    }

    @Override
    public int getMoney() {
        return super.getSocialValue() / 5;
    }
}
