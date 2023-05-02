import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.NoticeMessage;
import com.oocourse.spec3.main.Person;

public class MyNoticeMessage implements NoticeMessage {
    private String string;
    private int id;
    private int socialValue;
    private int type;
    private Person person1;
    private Person person2;
    private Group group;
    
    public MyNoticeMessage(int messageId, String noticeString,
                           Person messagePerson1, Person messagePerson2) {
        this.type = 0;
        this.group = null;
        this.id = messageId;
        this.person1 = messagePerson1;
        this.person2 = messagePerson2;
        this.string = noticeString;
        this.socialValue = noticeString.length();
    }
    
    public MyNoticeMessage(int messageId, String noticeString,
                           Person messagePerson1, Group messageGroup) {
        this.type = 1;
        this.person2 = null;
        this.id = messageId;
        this.person1 = messagePerson1;
        this.group = messageGroup;
        this.string = noticeString;
        this.socialValue = noticeString.length();
    }
    
    public String getString() {
        return this.string;
    }
    
    public int getType() {
        return this.type;
    }
    
    public int getId() {
        return this.id;
    }
    
    public int getSocialValue() {
        return this.socialValue;
    }
    
    public Person getPerson1() {
        return this.person1;
    }
    
    public Person getPerson2() {
        return this.person2;
    }
    
    public Group getGroup() {
        return this.group;
    }
    
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (!(obj instanceof Message)) {
            return false;
        } else {
            return (((Message) obj).getId() == this.id);
        }
    }
}
