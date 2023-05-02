public class TestThreadRunStart {

    public static void main(String[] args) {
        Thread t = new Thread() {
            @Override
            public void run() {
                System.out.println("interrupted: " + interrupted());
                //休眠3秒
                try {
                    Thread.sleep(3000);
                    System.out.println("休眠3秒");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Thread running...");
            }
        };
        System.out.println("test Run");
        testRun(t);

        System.out.println("\ntest Start");
        testStart(t);
    }

    private static void testRun(Thread t) {
        t.run();  // run是串联执行，无法达到启动多线程的目的
        t.interrupt();  // 执行完run才会执行interrupt interrupt不生效
        //休眠1秒
        try {
            Thread.sleep(1000);
            System.out.println("休眠1秒");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void testStart(Thread t) {
        t.start(); // start会自动调用run()方法，启动多线程
        t.interrupt();
        //休眠1秒
        try {
            Thread.sleep(1000);
            System.out.println("休眠1秒");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}