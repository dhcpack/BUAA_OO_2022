public interface Observerable {
    void addObserver(Observer observer);

    void removeObserver(Observer observer);

    void notifyObserver(String msg) throws InterruptedException; //打印msg并调用每个注册过的观察者的update方法
}