public class User implements Observer, Runnable {
    private String name;
    private String msg;

    public User(String name) {
        this.name = name;
    }

    public void run() {
        synchronized (User.class) {
            update(msg);
        }

    }

    public void update(String msg) {
        System.out.println(name + ": " + msg);

    } //在该方法中打印msg

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
