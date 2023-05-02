package society.message;

import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Person;

public class SocialMessage implements Message {
    private int id;
    private int socialValue;
    private int type;
    private Person person1;
    private Person person2;
    private Group group;

    public SocialMessage(int id, int socialValue, Person person1, Person person2) {
        this.id = id;
        this.socialValue = socialValue;
        this.person1 = person1;
        this.person2 = person2;
        this.type = 0;
        this.group = null;
    }

    public SocialMessage(int id, int socialValue, Person person1, Group group) {
        this.id = id;
        this.socialValue = socialValue;
        this.person1 = person1;
        this.group = group;
        this.type = 1;
        this.person2 = null;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getSocialValue() {
        return socialValue;
    }

    @Override
    public Person getPerson1() {
        return person1;
    }

    @Override
    public Person getPerson2() {
        return person2;
    }

    @Override
    public Group getGroup() {
        return group;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Message) {
            return ((Message) obj).getId() == id;
        }
        return false;
    }
}
