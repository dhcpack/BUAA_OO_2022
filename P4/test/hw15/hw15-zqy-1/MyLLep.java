import java.util.HashSet;

public abstract class MyLLep {
    private final HashSet<String> msgSent = new HashSet<>();
    private final HashSet<String> msgRecv = new HashSet<>();
    private boolean violateRule007 = false;
    private boolean destroyed = false;

    public boolean isViolatedRule007() {
        return violateRule007;
    }

    public void recvMsg(boolean isDestroy) {
        if (destroyed) {
            violateRule007 = true;
        }
        if (isDestroy) {
            destroyed = true;
        }
    }

    public HashSet<String> getMsgSent() {
        return msgSent;
    }

    public HashSet<String> getMsgRecv() {
        return msgRecv;
    }
}
