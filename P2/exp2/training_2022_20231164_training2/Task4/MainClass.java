public class MainClass {
    public static void main(String[] args) {
        Tray tray = new Tray(1);
        Customer customer = new Customer(tray);
        Producer producer = new Producer(tray);

        Thread customerThread = new Thread(customer);
        Thread producerThread = new Thread(producer);

        customerThread.start();
        producerThread.start();
    }
}