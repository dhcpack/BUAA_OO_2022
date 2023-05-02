import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Adventurer implements ValueBody {
    private int id;
    private String name;
    private double health = 100.0;
    private double exp = 0.0;
    private double money = 0.0;
    private HashMap<Integer, ValueBody> valueBodyHashMap = new HashMap<>();

    public Adventurer(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public double getExp() {
        return exp;
    }

    public void setExp(double exp) {
        this.exp = exp;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public void addValueBody(ValueBody valueBody) {
        valueBodyHashMap.put(valueBody.getId(), valueBody);
    }

    public void deleteValueBody(int id) {
        valueBodyHashMap.remove(id);
    }

    @Override
    public BigInteger getPrice() {
        BigInteger sumOfPrice = new BigInteger("0");
        for (ValueBody valueBody : valueBodyHashMap.values()) {
            sumOfPrice = sumOfPrice.add(valueBody.getPrice());
        }
        return sumOfPrice;
    }

    public BigInteger getMaxPrice() {
        BigInteger maxPrice = new BigInteger("0");
        for (ValueBody valueBody : valueBodyHashMap.values()) {
            if (valueBody.getPrice().compareTo(maxPrice) > 0) {
                maxPrice = valueBody.getPrice();
            }
        }
        return maxPrice;
    }

    public int getSumOfValueBodies() {
        return valueBodyHashMap.size();
    }

    public String getValueBodyProperty(int id) {
        ValueBody valueBody = valueBodyHashMap.get(id);
        return valueBody.toString();
    }

    @Override
    public String toString() {
        return "The adventurer's id is " + getId() + ", name is " + getName() + ", health is "
                + getHealth() + ", exp is " + getExp() + ", money is " + getMoney() + ".";
    }

    @Override
    public void use(Adventurer user) {
        ArrayList<ValueBody> valueBodies = new ArrayList<>(valueBodyHashMap.values());
        Collections.sort(valueBodies, (o1, o2) -> {
            if (o1.getPrice().compareTo(o2.getPrice()) != 0) {
                return o2.getPrice().compareTo(o1.getPrice());
            } else if (o1.getId() > o2.getId()) {
                return -1;
            }
            return 1;
        });
        for (ValueBody valueBody : valueBodies) {
            try {
                valueBody.use(user);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
