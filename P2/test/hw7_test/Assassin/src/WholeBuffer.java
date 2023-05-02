import java.util.ArrayList;

public class WholeBuffer {
    private ArrayList<PersonRequest2> whole;
    private int readend;

    public synchronized int getReadend() {
        return readend;
    }

    public synchronized void setReadend(int readend) {
        this.readend = readend;
        notifyAll();
    }

    public WholeBuffer() {
        this.whole = new ArrayList<>();
        readend = Const.PRONOTEND;
    }

    public synchronized void put(PersonRequest2 p) {
        whole.add(p);
        notifyAll();
    }

    public synchronized boolean isempty() {
        return whole.isEmpty();
    }

    public synchronized PersonRequest2 get() {
        PersonRequest2 p = whole.get(0);
        whole.remove(p);
        return p;
    }

    public synchronized void check() throws InterruptedException {
        wait();
    }

}

