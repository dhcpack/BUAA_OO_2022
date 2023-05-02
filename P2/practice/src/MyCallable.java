import java.util.concurrent.Callable;

public class MyCallable implements Callable<Integer>{
    public Integer call() {
        return 123;
    }
}
