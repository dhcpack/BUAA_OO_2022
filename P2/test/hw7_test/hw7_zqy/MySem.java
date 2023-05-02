public class MySem {
    private int count = 0;

    public synchronized void inc() {
        count++;
        notifyAll();
    }

    public synchronized void dec() {
        count--;
        notifyAll();
    }

    public synchronized void waitForChange() throws InterruptedException {
        wait();
    }

    public synchronized int getCount() {
        return count;
    }
}
