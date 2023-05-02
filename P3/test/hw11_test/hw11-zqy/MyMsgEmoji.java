import com.oocourse.spec3.main.EmojiMessage;
import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Person;

public class MyMsgEmoji extends MyMsg implements EmojiMessage {
    private final int emojiId;

    public MyMsgEmoji(int id, int emojiId, Person p1, Person p2) {
        super(id, emojiId, p1, p2);
        this.emojiId = emojiId;
    }

    public MyMsgEmoji(int id, int emojiId, Person p1, Group grp) {
        super(id, emojiId, p1, grp);
        this.emojiId = emojiId;
    }

    public int getEmojiId() {
        return emojiId;
    }
}
