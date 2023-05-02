public class HealingPotion extends Bottle {
    private double efficiency;

    public HealingPotion(int id, String name, long price, double capacity, double efficiency) {
        super(id, name, price, capacity);
        this.efficiency = efficiency;
    }

    public double getEfficiency() {
        return efficiency;
    }

    @Override
    public String toString() {
        return "The healingPotion's id is " + super.getId() + ", name is " + super.getName() + "," +
                " capacity is " + super.getCapacity() + ", filled is " + super.isFilled() + ", " +
                "efficiency is " + efficiency + ".";
    }

    @Override
    public void use(Adventurer user) throws Exception {
        if (!isFilled()) {
            throw new Exception("Failed to use " + getName() + " because it is empty.");
        }
        user.setHealth(user.getHealth() + getCapacity() / 10 + getCapacity() * getEfficiency());
        setFilled(false);
        setPrice(getPrice().longValue() / 10);
        System.out.println(user.getName() + " drank " + getName() + " and recovered " +
                getCapacity() / 10 + ".");
        System.out.println(user.getName() + " recovered extra " +
                getCapacity() * getEfficiency() + ".");
    }
}
