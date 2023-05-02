import java.util.ArrayList;

public class MySet implements IntSet {
    private ArrayList<Integer> arr;
    private int count;

    public MySet() {
        arr = new ArrayList<>();
        count = 0;
    }

    @Override
    public Boolean contains(int x) {
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i) == x) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getNum(int x) throws IndexOutOfBoundsException {
        if (x >= 0 && x < count) {
            return arr.get(x);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public void insert(int x) {
        int left = 0;
        int right = count - 1;
        int pos = -1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (arr.get(mid) >= x) {
                pos = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        if (pos == -1) {
            count++;
            arr.add(x);
        } else {
            arr.add(pos, x);
            count++;
        }
    }

    @Override
    public void delete(int x) {
        int left = 0;
        int right = count - 1;
        int pos = -1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (arr.get(mid) >= x) {
                pos = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        if (pos != -1 && arr.get(pos) == x) {
            arr.remove(pos);
            count--;
        }
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public void elementSwap(IntSet a) {
        ArrayList<Integer> newArr = new ArrayList<>();
        for (int i = 0; i < a.size(); i++) {
            newArr.add(a.getNum(i));
        }
        for (Integer number : newArr) {
            a.delete(number);
        }
        for (Integer number : arr) {
            a.insert(number);
        }
        ArrayList<Integer> temp = new ArrayList<>(arr);
        for (Integer number : temp) {
            delete(number);
        }
        for (Integer number : newArr) {
            insert(number);
        }
    }

    @Override
    public IntSet symmetricDifference(IntSet a) throws NullPointerException {
        IntSet intSet = new MySet();
        for (int i = 0; i < size(); i++) {
            intSet.insert(getNum(i));
        }
        for (int i = 0; i < size(); i++) {
            if (intSet.contains(a.getNum(i))) {
                intSet.delete(a.getNum(i));
            } else {
                intSet.insert(a.getNum(i));
            }
        }
        return intSet;
    }

    @Override
    public boolean repOK() {
        for (int i = 0; i < size() - 1; i++) {
            if (!(getNum(i) < getNum(i + 1))) {
                return false;
            }
        }
        return true;
    }
}
