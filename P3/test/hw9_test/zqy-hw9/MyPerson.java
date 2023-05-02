import com.oocourse.spec1.main.Person;

public class MyPerson implements Person {
    private final int id;
    private final String name;
    private final int age;

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        MyGlobal.addPerson(this);
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
        if (obj == null || !(obj instanceof Person)) {
            return false;
        }
        return ((Person) obj).getId() == id;
    }

    public boolean isLinked(Person person) {
        if (id == person.getId()) {
            return true;
        }
        if (person instanceof MyPerson) {
            return MyGlobal.haveRelation(id, person.getId());
        }
        // TODO: exchange, is this necessary ?
        return person.isLinked(this);
    }

    public int queryValue(Person person) {
        if (person instanceof MyPerson) {
            Integer value = MyGlobal.queryValue(id, person.getId());
            if (value == null) {
                return 0;
            }
            return value;
        }
        // exchange
        // TODO: exchange, is this necessary ?
        return person.queryValue(this);
    }

    public int compareTo(Person rhs) {
        return name.compareTo(rhs.getName());
    }
}
