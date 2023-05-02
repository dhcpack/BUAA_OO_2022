public class SwitchInfo {
    private final int value;

    public SwitchInfo(int switchInfo) {
        this.value = switchInfo;
    }

    public boolean canOpenAt(char building) {
        return ((value >> (building - 'A')) & 1) == 1;
    }
}
