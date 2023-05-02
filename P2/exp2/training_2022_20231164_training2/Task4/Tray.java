public class Tray {
    int capacity;
    int currentNum;
    int production;

    public Tray(int capacity) {
        this.capacity = capacity;
        this.currentNum = 0;
    }

    public synchronized void putProduction(int index) {  // 同步的本质是 在传送带上 放物品必须在取物品之前
        while (currentNum == capacity) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.production = index;
        currentNum++;
        System.out.println("Producer put:" + index);
        notifyAll();
        // try {
        //     sleep((int) (Math.random() * 100));
        // } catch (InterruptedException e) {
        //     e.printStackTrace();
        // }   // 这段代码放到ThreadProducer中不共用的部分实现，能够让ThreadCustom提早发生
    }

    public synchronized int getProduction() {
        while (currentNum == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        currentNum--;
        System.out.println("Consumer get:" + production);
        notifyAll();
        return production;
    }


}
