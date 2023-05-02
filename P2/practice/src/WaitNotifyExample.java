public class WaitNotifyExample {
    public synchronized void before() {
        System.out.println("before");
        notify();
    }

    public synchronized void after() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("after");
    }
}
