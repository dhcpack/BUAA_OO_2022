public class SynchronizedExample implements Runnable {
    private int count;
    public static int staticCount;

    public void run() {
        // synchronized (this) {
        //     for (int i = 0; i < 10; i++) {
        //         System.out.println(Thread.currentThread().getName() + ": " + count);
        //         count++;
        //     }
        // }
        synchronizedStaticFunc();
    }

    public void func() {
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + ": " + count);
            count++;
        }
    }

    public synchronized void synchronizedFunc() {
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + ": " + count);
            count++;
        }
    }

    public void synchronizedClass() {
        synchronized (SynchronizedExample.class) {
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName() + ": " + count);
                count++;
            }
        }
    }

    public static synchronized void synchronizedStaticFunc() {
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + ": " + staticCount);
            staticCount++;
        }
    }


    public void getCount() {
        System.out.println(count);
    }


    public static void main(String[] args) throws InterruptedException {
        SynchronizedExample e1 = new SynchronizedExample();
        SynchronizedExample e2 = new SynchronizedExample();
        Thread thread1 = new Thread(e1, "thread1");
        Thread thread2 = new Thread(e2, "thread2");

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println();
        e1.getCount();
        e2.getCount();
    }
}
