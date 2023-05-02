import java.math.BigInteger;

public class Equipment implements ValueBody {
    private int id;
    private String name;
    private long price;

    public Equipment(int id, String name, long price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    @Override
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public BigInteger getPrice() {
        return new BigInteger(Long.toString(price));
    }

    public void setPrice(long price) {
        this.price = price;
    }

    @Override
    public void use(Adventurer user) throws Exception {
    }
}


