import com.oocourse.spec2.main.Runner;

public class MainClass {
    private static int ValueSum = 0;

    public static void main(String[] args) throws Exception {
        Runner runner = new Runner(MyPerson.class, MyNetwork.class, MyGroup.class, MyMessage.class);
        runner.run();
    }

    public static int getValueSum() {
        return ValueSum;
    }

    public static void setValueSum(int valueSum) {
        ValueSum = valueSum;
    }
}

