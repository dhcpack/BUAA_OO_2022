import java.math.BigInteger;

public interface ValueBody {
    public BigInteger getPrice();

    public void use(Adventurer user) throws Exception;

    public String toString();

    public int getId();

}
