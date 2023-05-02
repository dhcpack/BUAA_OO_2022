public class ExpBottle extends Bottle {
    private double expRatio;

    public ExpBottle(int id, String name, long price, double capacity, double expRatio) {
        super(id, name, price, capacity);
        this.expRatio = expRatio;
    }

    public double getExpRatio() {
        return expRatio;
    }

    @Override
    public String toString() {
        return "The expBottle's id is " + super.getId() + ", name is " + super.getName() + ", " +
                "capacity is " + super.getCapacity() + ", filled is " + super.isFilled() + ", " +
                "expRatio is " + expRatio + ".";
    }

    @Override
    public void use(Adventurer user) throws Exception {
        if (!isFilled()) {
            throw new Exception("Failed to use " + getName() + " because it is empty.");
        }
        user.setHealth(user.getHealth() + getCapacity() / 10);
        user.setExp(user.getExp() * getExpRatio());
        setFilled(false);
        setPrice(getPrice().longValue() / 10);
        System.out.println(user.getName() + " drank " + getName() + " and recovered " +
                getCapacity() / 10 + ".");
        System.out.println(user.getName() + "'s exp became " +
                user.getExp() + ".");
    }
}
