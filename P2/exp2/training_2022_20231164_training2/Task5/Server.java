import java.util.ArrayList;

public class Server implements Observerable {
    ArrayList<Observer> observers = new ArrayList<>();

    public void addObserver(Observer observer) {
        observers.add(observer);

    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);

    }

    public void notifyObserver(String msg) throws InterruptedException {
        System.out.println("server: " + msg);
        for (Observer observer : observers) {
            User user = (User) observer;
            user.setMsg(msg);
            Thread thread = new Thread(user);
            thread.start();
            thread.join();
        }
    }//打印msg并调用每个注册过的观察者的update方法
}
