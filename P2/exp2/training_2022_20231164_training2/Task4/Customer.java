public class Customer extends Thread {
    private Tray tray;

    public Customer(Tray tray) {
        this.tray = tray;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            tray.getProduction();
        }
    }
}
