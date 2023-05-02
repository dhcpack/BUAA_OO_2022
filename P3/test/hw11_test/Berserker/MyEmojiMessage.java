import com.oocourse.spec3.main.EmojiMessage;
import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Person;

public class MyEmojiMessage extends MyMessage implements EmojiMessage {

    public MyEmojiMessage(int messageId, int emojiId,
                          Person messagePerson1, Person messagePerson2) {
        super(messageId, emojiId, messagePerson1, messagePerson2);
    }

    public MyEmojiMessage(int messageId, int emojiId,
                          Person messagePerson1, Group messageGroup) {
        super(messageId, emojiId, messagePerson1, messageGroup);
    }

    @Override
    public int getEmojiId() {
        return super.getSocialValue();
    }
}
