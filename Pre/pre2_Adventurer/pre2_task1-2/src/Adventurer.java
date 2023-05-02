import java.math.BigInteger;
import java.util.ArrayList;

public class Adventurer {
    private int id;
    private String name;
    private ArrayList<Bottle> bottles = new ArrayList<>();
    private BigInteger sumOfPrice = new BigInteger("0");

    public Adventurer(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addBottle(Bottle bottle) {
        bottles.add(bottle);
        BigInteger newPrice = new BigInteger(Long.toString(bottle.getPrice()));
        sumOfPrice = sumOfPrice.add(newPrice);
    }

    public void deleteBottle(int id) {
        Bottle bottle = findBottle(id);
        bottles.remove(bottle);
        BigInteger newPrice = new BigInteger(Long.toString(bottle.getPrice()));
        sumOfPrice = sumOfPrice.subtract(newPrice);
    }

    public Bottle findBottle(int id) {
        for (Bottle bottle : bottles) {
            if (bottle.getId() == id) {
                return bottle;
            }
        }
        return new Bottle(0, "0", 0, 0.0);
    }

    public BigInteger getSumPrice() {
        return sumOfPrice;
    }

    public long getMaxPrice() {
        long maxPrice = 0;
        for (Bottle bottle : bottles) {
            if (bottle.getPrice() > maxPrice) {
                maxPrice = bottle.getPrice();
            }
        }
        return maxPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
