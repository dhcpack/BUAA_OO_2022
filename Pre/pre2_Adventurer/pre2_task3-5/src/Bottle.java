public class Bottle extends Equipment {
    private double capacity;
    private boolean filled = true;

    public Bottle(int id, String name, long price, double capacity) {
        super(id, name, price);
        this.capacity = capacity;
    }

    public double getCapacity() {
        return capacity;
    }

    public boolean isFilled() {
        return filled;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    public String toString() {
        return "The bottle's id is " + super.getId() + ", name is " + super.getName() + ", " +
                "capacity is " + capacity + ", filled is " + filled + ".";
    }

    @Override
    public void use(Adventurer user) throws Exception {
        if (!isFilled()) {
            throw new Exception("Failed to use " + getName() + " because it is empty.");
        }
        user.setHealth(user.getHealth() + getCapacity() / 10);
        setFilled(false);
        setPrice(getPrice().longValue() / 10);
        System.out.println(user.getName() + " drank " + getName() + " and recovered " +
                getCapacity() / 10 + ".");
    }

}