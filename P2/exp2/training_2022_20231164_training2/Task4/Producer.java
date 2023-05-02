public class Producer extends Thread {
    private Tray tray;

    public Producer(Tray tray) {
        this.tray = tray;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            tray.putProduction(i);
            try {
                sleep((int) (Math.random() * 100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
