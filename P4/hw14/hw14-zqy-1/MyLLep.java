import java.util.HashSet;

public abstract class MyLLep {
    private final HashSet<String> msgSent = new HashSet<>();
    private final HashSet<String> msgRecv = new HashSet<>();

    public HashSet<String> getMsgSent() {
        return msgSent;
    }

    public HashSet<String> getMsgRecv() {
        return msgRecv;
    }
}
