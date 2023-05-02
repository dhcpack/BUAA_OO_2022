import java.util.HashSet;

public abstract class MyInterCls {
    private final HashSet<String> attrs = new HashSet<>();
    private final HashSet<String> genTgts = new HashSet<>();
    private final HashSet<String> genSrcs = new HashSet<>();

    public HashSet<String> getAttrs() {
        return attrs;
    }

    public HashSet<String> getGenTgts() {
        return genTgts;
    }

    public HashSet<String> getGenSrcs() {
        return genSrcs;
    }
}
