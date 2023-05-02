import com.sun.deploy.panel.JHighDPITable;

import java.util.HashSet;

public class kt {
    public static void main(String[] args) throws InterruptedException {
        HashSet<Thread> threadHashSet = new HashSet<>();
        System.out.println("使用关键字synchronized");
        SyncThread syncThread1 = new SyncThread();
        SyncThread syncThread2 = new SyncThread();
        Thread thread1 = new Thread(syncThread1, "SyncThread1");
        Thread thread2 = new Thread(syncThread2, "SyncThread2");
        threadHashSet.add(thread1);
        threadHashSet.add(thread2);
        for (Thread thread : threadHashSet) {
            thread.start();
        }
        for (Thread thread : threadHashSet) {
            thread.join();
        }
        System.out.println(syncThread1.getCount());
        System.out.println(syncThread2.getCount());
    }
}

class SyncThread implements Runnable {
    private static int count;

    public SyncThread() {
        count = 0;
    }

    public void run() {
        synchronized (this) {
            for (int i = 0; i < 5; i++) {
                try {
                    System.out.println("线程名:" + Thread.currentThread().getName() + ":" + (count++));
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public int getCount() {
        return count;
    }
}