public class MainClass {
    public static void main(String[] args) {
        new SimpleThread("t1").start();
        new SimpleThread("t2").start();
    }
}