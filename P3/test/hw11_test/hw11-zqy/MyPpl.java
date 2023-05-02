import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Person;

public class MyPpl implements Person {
    private final int id;
    private final String name;
    private final int age;
    private final HashMap<Integer, Integer> linkVals = new HashMap<>();
    private int socVal = 0;
    private final ArrayList<MyMsg> msgs = new ArrayList<>();
    private int money = 0;

    public MyPpl(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof Person)) {
            return false;
        }
        return ((Person) o).getId() == id;
    }

    public boolean isLinked(Person p) {
        return linkVals.containsKey(p.getId()) || p.getId() == id;
    }

    public int queryValue(Person p) {
        return linkVals.getOrDefault(p.getId(), 0);
    }

    public int compareTo(Person p) {
        return name.compareTo(p.getName());
    }

    public void addSocialValue(int num) {
        socVal += num;
    }

    public int getSocialValue() {
        return socVal;
    }

    public List<Message> getMessages() {
        return msgs.stream().map(m -> (Message) m).collect(Collectors.toList());
    }

    public List<Message> getReceivedMessages() {
        int count = Integer.min(msgs.size(), 4);
        return msgs.subList(0, count).stream().map(m -> (Message) m).collect(Collectors.toList());
    }

    public void addMoney(int num) {
        money += num;
    }

    public int getMoney() {
        return money;
    }

    // below for my use

    public HashMap<Integer, Integer> myGetLinks() {
        return linkVals;
    }

    public ArrayList<MyMsg> myGetMsgs() {
        return msgs;
    }
}
