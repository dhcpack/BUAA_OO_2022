import java.util.List;
import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;
import com.oocourse.spec3.exceptions.EqualEmojiIdException;
import com.oocourse.spec3.exceptions.EqualGroupIdException;
import com.oocourse.spec3.exceptions.EqualMessageIdException;
import com.oocourse.spec3.exceptions.EqualPersonIdException;
import com.oocourse.spec3.exceptions.EqualRelationException;
import com.oocourse.spec3.exceptions.GroupIdNotFoundException;
import com.oocourse.spec3.exceptions.MessageIdNotFoundException;
import com.oocourse.spec3.exceptions.PersonIdNotFoundException;
import com.oocourse.spec3.exceptions.RelationNotFoundException;
import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Network;
import com.oocourse.spec3.main.Person;

public class MyNet implements Network {
    public MyNet() {}

    public boolean contains(int id) {
        return MyGlb.contains(id);
    }

    public Person getPerson(int id) {
        return MyGlb.getPerson(id);
    }

    public void addPerson(Person p) throws EqualPersonIdException {
        MyGlb.addPerson((MyPpl) p);
    }

    public void addRelation(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualRelationException {
        MyGlb.addRelation(id1, id2, value);
    }

    public int queryValue(int id1, int id2)
            throws PersonIdNotFoundException, RelationNotFoundException {
        return MyGlb.queryValue(id1, id2);
    }

    public int queryPeopleSum() {
        return MyGlb.queryPeopleSum();
    }

    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        return MyGlb.isCircle(id1, id2);
    }

    public int queryBlockSum() {
        return MyGlb.queryBlockSum();
    }

    public int queryLeastConnection(int id) throws PersonIdNotFoundException {
        return MyGlb.queryLeastConnection(id);
    }

    public void addGroup(Group grp) throws EqualGroupIdException {
        MyGlb.addGroup((MyGrp) grp);
    }

    public Group getGroup(int id) {
        return MyGlb.getGroup(id);
    }

    public void addToGroup(int ppId, int grpId)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        MyGlb.addToGroup(ppId, grpId);
    }

    public int queryGroupPeopleSum(int id) throws GroupIdNotFoundException {
        return MyGlb.queryGroupPeopleSum(id);
    }

    public int queryGroupValueSum(int id) throws GroupIdNotFoundException {
        return MyGlb.queryGroupValueSum(id);
    }

    public int queryGroupAgeVar(int id) throws GroupIdNotFoundException {
        return MyGlb.queryGroupAgeVar(id);
    }

    public void delFromGroup(int ppId, int grpId)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        MyGlb.delFromGroup(ppId, grpId);
    }

    public boolean containsMessage(int id) {
        return MyGlb.containsMessage(id);
    }

    public void addMessage(Message message)
            throws EqualMessageIdException, EmojiIdNotFoundException, EqualPersonIdException {
        MyGlb.addMessage((MyMsg) message);
    }

    public Message getMessage(int id) {
        return MyGlb.getMessage(id);
    }

    public void sendMessage(int id) throws RelationNotFoundException, MessageIdNotFoundException,
            PersonIdNotFoundException {
        MyGlb.sendMessage(id);
    }

    public int querySocialValue(int id) throws PersonIdNotFoundException {
        return MyGlb.querySocialValue(id);
    }

    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        return MyGlb.queryReceivedMessages(id);
    }

    public boolean containsEmojiId(int id) {
        return MyGlb.containsEmojiId(id);
    }

    public void storeEmojiId(int id) throws EqualEmojiIdException {
        MyGlb.storeEmojiId(id);
    }

    public int queryMoney(int id) throws PersonIdNotFoundException {
        return MyGlb.queryMoney(id);
    }

    public int queryPopularity(int id) throws EmojiIdNotFoundException {
        return MyGlb.queryPopularity(id);
    }

    public int deleteColdEmoji(int limit) {
        return MyGlb.deleteColdEmoji(limit);
    }

    public void clearNotices(int personId) throws PersonIdNotFoundException {
        MyGlb.clearNotices(personId);
    }

    public int sendIndirectMessage(int id) throws MessageIdNotFoundException {
        return MyGlb.sendIndirectMessage(id);
    }
}
