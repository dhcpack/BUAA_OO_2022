package main;

import com.oocourse.spec2.exceptions.EqualGroupIdException;
import com.oocourse.spec2.exceptions.EqualMessageIdException;
import com.oocourse.spec2.exceptions.EqualPersonIdException;
import com.oocourse.spec2.exceptions.EqualRelationException;
import com.oocourse.spec2.exceptions.GroupIdNotFoundException;
import com.oocourse.spec2.exceptions.MessageIdNotFoundException;
import com.oocourse.spec2.exceptions.PersonIdNotFoundException;
import com.oocourse.spec2.exceptions.RelationNotFoundException;
import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Network;
import com.oocourse.spec2.main.Person;
import exceptions.MyEqualGroupIdException;
import exceptions.MyEqualMessageIdException;
import exceptions.MyEqualPersonIdException;
import exceptions.MyEqualRelationException;
import exceptions.MyGroupIdNotFoundException;
import exceptions.MyMessageIdNotFoundException;
import exceptions.MyPersonIdNotFoundException;
import exceptions.MyRelationNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyNetwork implements Network {
    private final ArrayList<Person> people;
    private final ArrayList<Group> groups;
    private final HashMap<Integer, Integer> map;
    private final int[] father;
    private int size;
    private int setNumber;
    private final ArrayList<Message> messages;
    private final ArrayList<Edge> edges;

    public MyNetwork() {
        people = new ArrayList<>();
        groups = new ArrayList<>();
        messages = new ArrayList<>();
        edges = new ArrayList<>();
        father = new int[20000];
        map = new HashMap<>();
        setNumber = 0;
        size = 0;
    }

    public void addElement(int x) {
        map.put(x, size);
        size++;
        father[map.get(x)] = map.get(x);
        setNumber++;
    }

    public void union(int x, int y) {
        int root1 = find(x);
        int root2 = find(y);
        father[root1] = root2;
        if (root1 != root2) {
            setNumber--;
        }
    }

    public int find(int x) {
        if (x != father[x]) {
            father[x] = find(father[x]);
        }
        return father[x];
    }

    @Override
    public boolean contains(int id) {
        for (Person myPerson : people) {
            if (myPerson.getId() == id) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Person getPerson(int id) {
        if (contains(id)) {
            for (Person person : people) {
                if (person.getId() == id) {
                    return person;
                }
            }
        }
        return null;
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        boolean havePerson = false;
        for (Person person1 : people) {
            if (person1.equals(person)) {
                havePerson = true;
                break;
            }
        }
        if (!havePerson) {
            people.add(person);
            addElement(person.getId());
        } else {
            throw new MyEqualPersonIdException(person.getId());
        }
    }

    @Override
    public void addRelation(int id1, int id2, int value) throws
            PersonIdNotFoundException, EqualRelationException {
        boolean unlinked = contains(id1) && contains(id2)
                && !getPerson(id1).isLinked(getPerson(id2));
        if (unlinked) {
            MyPerson person1 = (MyPerson) getPerson(id1);
            MyPerson person2 = (MyPerson) getPerson(id2);
            person1.setAcquaintanceAndValue(getPerson(id2), value);
            person2.setAcquaintanceAndValue(getPerson(id1), value);
            union(map.get(id1), map.get(id2));
            if (edges.isEmpty()) {
                edges.add(new Edge(id1, id2, value));
            } else {
                Edge edge = new Edge(id1, id2, value);
                int size = edges.size();
                int index = -1;
                for (int i = 0; i < size; i++) {
                    if (edges.get(i).getValue() > value) {
                        index = i;
                        break;
                    }
                }
                if (index == -1) {
                    edges.add(edge);
                } else {
                    edges.add(index, edge);
                }
            }
            if (setNumber <= 0) {
                System.err.println("the id1 is " + id1 + ", the y is " + id2);
            }
        } else {
            if (!contains(id1)) {
                throw new MyPersonIdNotFoundException(id1);
            } else if (!contains(id2)) {
                throw new MyPersonIdNotFoundException(id2);
            } else {
                throw new MyEqualRelationException(id1, id2);
            }
        }
    }

    @Override
    public int queryValue(int id1, int id2) throws
            PersonIdNotFoundException, RelationNotFoundException {
        boolean requires = contains(id1) && contains(id2)
                && getPerson(id1).isLinked(getPerson(id2));
        if (requires) {
            return getPerson(id1).queryValue(getPerson(id2));
        } else {
            if (!contains(id1)) {
                throw new MyPersonIdNotFoundException(id1);
            } else if (!contains(id2)) {
                throw new MyPersonIdNotFoundException(id2);
            } else {
                throw new MyRelationNotFoundException(id1, id2);
            }
        }
    }

    @Override
    public int queryPeopleSum() {
        return people.size();
    }

    @Override
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        boolean require = contains(id1) && contains(id2);
        if (require) {
            int root1 = find(map.get(id1));
            int root2 = find(map.get(id2));
            return root1 == root2;
        } else {
            if (!contains(id1)) {
                throw new MyPersonIdNotFoundException(id1);
            } else if (!contains(id2)) {
                throw new MyPersonIdNotFoundException(id2);
            } else {
                return false;
            }
        }
    }

    @Override
    public int queryBlockSum() {
        return setNumber;
    }

    @Override
    public int queryLeastConnection(int id) throws PersonIdNotFoundException {
        boolean requires = contains(id);
        if (requires) {
            ArrayList<Edge> myEdges = new ArrayList<>();
            int root = find(map.get(id));
            for (Edge edge : edges) {
                int root1 = find(map.get(edge.getSt()));
                if (root == root1) {
                    myEdges.add(edge);
                }
            }
            // System.out.println("this are all edges:\n");
            // for (Edge edge : edges) {
            // System.out.println("the st is: " + edge.getSt() + ", the end is: "
            // + edge.getEnd() + ", the weight is: " + edge.getValue());
            // }
            // System.out.println("");
            // System.out.println("");
            // System.out.println("this is the block:\n");
            // for (Edge edge : myEdges) {
            // System.out.println("the st is: " + edge.getSt() + ", the end is: "
            // + edge.getEnd() + ", the weight is: " + edge.getValue());
            // }
            // System.out.println("");
            // System.out.println("");
            Kruskal kruskal = new Kruskal(myEdges);
            return kruskal.run();
        } else {
            throw new MyPersonIdNotFoundException(id);
        }
    }

    @Override
    public void addGroup(Group group) throws EqualGroupIdException {
        boolean haveGroup = false;
        for (Group group1 : groups) {
            if (group1.equals(group)) {
                haveGroup = true;
                break;
            }
        }
        if (!haveGroup) {
            groups.add(group);
        } else {
            throw new MyEqualGroupIdException(group.getId());
        }
    }

    @Override
    public Group getGroup(int id) {
        for (Group group : groups) {
            if (group.getId() == id) {
                return group;
            }
        }
        return null;
    }

    public boolean groupContain(int id) {
        for (Group group : groups) {
            if (group.getId() == id) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void addToGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        boolean requires = groupContain(id2) && contains(id1)
                && !getGroup(id2).hasPerson(getPerson(id1))
                && getGroup(id2).getSize() < 1111;
        if (requires) {
            getGroup(id2).addPerson(getPerson(id1));
        } else {
            if (!groupContain(id2)) {
                throw new MyGroupIdNotFoundException(id2);
            } else if (!contains(id1)) {
                throw new MyPersonIdNotFoundException(id1);
            } else if (getGroup(id2).hasPerson(getPerson(id1))) {
                throw new MyEqualPersonIdException(id1);
            }
        }
    }

    @Override
    public int queryGroupPeopleSum(int id) throws GroupIdNotFoundException {
        boolean requires = groupContain(id);
        if (requires) {
            return getGroup(id).getSize();
        } else {
            throw new MyGroupIdNotFoundException(id);
        }
    }

    @Override
    public int queryGroupValueSum(int id) throws GroupIdNotFoundException {
        boolean requires = groupContain(id);
        if (requires) {
            return getGroup(id).getValueSum();
        } else {
            throw new MyGroupIdNotFoundException(id);
        }
    }

    @Override
    public int queryGroupAgeVar(int id) throws GroupIdNotFoundException {
        boolean requires = groupContain(id);
        if (requires) {
            return getGroup(id).getAgeVar();
        } else {
            throw new MyGroupIdNotFoundException(id);
        }
    }

    @Override
    public void delFromGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        boolean requires = groupContain(id2) && contains(id1)
                && getGroup(id2).hasPerson(getPerson(id1));
        if (requires) {
            getGroup(id2).delPerson(getPerson(id1));
        } else {
            if (!groupContain(id2)) {
                throw new MyGroupIdNotFoundException(id2);
            } else if (!contains(id1)) {
                throw new MyPersonIdNotFoundException(id1);
            } else if (!getGroup(id2).hasPerson(getPerson(id1))) {
                throw new MyEqualPersonIdException(id1);
            }
        }
    }

    @Override
    public boolean containsMessage(int id) {
        for (Message message : messages) {
            if (message.getId() == id) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void addMessage(Message message) throws EqualMessageIdException, EqualPersonIdException {
        boolean requires;
        boolean exist = false;
        for (Message message1 : messages) {
            if (message1.equals(message)) {
                exist = true;
                break;
            }
        }
        boolean valid = message.getType() != 0 || message.getPerson1() != message.getPerson2();
        requires = !exist & valid;
        if (requires) {
            messages.add(message);
        } else {
            if (exist) {
                throw new MyEqualMessageIdException(message.getId());
            } else {
                throw new MyEqualPersonIdException(message.getPerson1().getId());
            }
        }
    }

    @Override
    public Message getMessage(int id) {
        boolean requires = containsMessage(id);
        if (requires) {
            for (Message message : messages) {
                if (message.getId() == id) {
                    return message;
                }
            }
        }
        return null;
    }

    @Override
    public void sendMessage(int id) throws RelationNotFoundException,
            MessageIdNotFoundException, PersonIdNotFoundException {
        boolean have = containsMessage(id);
        if (!have) {
            throw new MyMessageIdNotFoundException(id);
        }
        Message message = getMessage(id);
        int type = getMessage(id).getType();
        Person person1 = message.getPerson1();
        Person person2 = message.getPerson2();
        Group group = message.getGroup();
        boolean require1 = type == 0 && person1.isLinked(person2) && person1 != person2;
        boolean require2 = type == 1 && group.hasPerson(person1);
        if (require1) {
            person1.addSocialValue(message.getSocialValue());
            person2.addSocialValue(message.getSocialValue());
            messages.remove(message);
            ((MyPerson) person2).addMessage(message);
        } else if (require2) {
            messages.remove(message);
            for (Person person : people) {
                if (group.hasPerson(person)) {
                    person.addSocialValue(message.getSocialValue());
                }
            }
        } else {
            if (type == 0 && !person1.isLinked(person2)) {
                throw new MyRelationNotFoundException(person1.getId(), person2.getId());
            } else if (type == 1 && !group.hasPerson(person1)) {
                throw new MyPersonIdNotFoundException(person1.getId());
            }
        }
    }

    @Override
    public int querySocialValue(int id) throws PersonIdNotFoundException {
        boolean requires = contains(id);
        if (requires) {
            return getPerson(id).getSocialValue();
        } else {
            throw new MyPersonIdNotFoundException(id);
        }
    }

    @Override
    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        boolean requires = contains(id);
        if (requires) {
            return getPerson(id).getReceivedMessages();
        } else {
            throw new MyPersonIdNotFoundException(id);
        }
    }
}
