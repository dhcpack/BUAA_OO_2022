public class SimpleThread extends Thread {
    public SimpleThread(String str) {
        super(str);
    }

    public void run() {
        synchronized (this) {
            for (int i = 0; i < 10; i++) {
                System.out.println(i + " " + getName());
                try {
                    sleep((long) (Math.random() * 1000));
                } catch (InterruptedException e) {
                }
            }
            System.out.println("DONE!" + getName());
        }

    }
}