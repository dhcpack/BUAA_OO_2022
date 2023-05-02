import com.oocourse.spec1.main.Person;

import java.util.ArrayList;
import java.util.HashMap;

public class MyPerson implements Person {
    private final int id;
    private final String name;
    private final int age;
    private final HashMap<MyPerson, Integer> acquaintances;
    private static ArrayList<MyPerson> people;
    private static boolean flag = true;
    
    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.age = age;
        this.name = name;
        this.acquaintances = new HashMap<>();
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
    
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Person) {
            return (((Person) obj).getId() == id);
        }
        return false;
    }
    
    public boolean isLinked(Person person) {
        if (person.getId() == id) {
            return true;
        }
        for (MyPerson item : acquaintances.keySet()) {
            if (item.getId() == person.getId()) {
                return true;
            }
        }
        return false;
    }
    
    public void dfs(MyPerson p2) {
        if (flag) {
            return;
        }
        if (this.equals(p2)) {
            flag = true;
            return;
        }
        people.add(this);
        for (MyPerson p1 : acquaintances.keySet()) {
            if (!people.contains(p1)) {
                p1.dfs(p2);
            }
        }
    }
    
    public boolean isCircle(MyPerson p2) {
        people = new ArrayList<>();
        flag = false;
        dfs(p2);
        return flag;
    }
    
    public int queryValue(Person person) {
        for (MyPerson item : acquaintances.keySet()) {
            if (item.getId() == person.getId()) {
                return acquaintances.get(item);
            }
        }
        return 0;
    }
    
    public int compareTo(Person p2) {
        return name.compareTo(p2.getName());
    }
    
    public void setLinked(MyPerson person, int value) {
        acquaintances.put(person, value);
    }
}
